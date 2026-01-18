docker build -t java-app:latest ./app
docker build -t go-app:latest ./payment
docker build -t challenge-english:latest ./challenge_english

# Carregar no Kind
kind load docker-image java-app:latest --name app-mfa-validet
kind load docker-image go-app:latest --name app-mfa-validet
kind load docker-image challenge-english:latest --name app-mfa-validet

# Criar namespace
kubectl apply -f projects/k8s/namespace.yaml

# Deploy (ordem importa - infraestrutura primeiro)
kubectl apply -f projects/k8s/base/postgres/ -n dev
kubectl apply -f projects/k8s/base/rabbitmq/ -n dev
kubectl apply -f projects/k8s/base/redis/ -n dev
kubectl apply -f projects/k8s/base/java-app/ -n dev
kubectl apply -f projects/k8s/base/go-app/ -n dev
kubectl apply -f projects/k8s/base/challenge-english/ -n dev

# Verificar
kubectl get pods -n dev
kubectl get services -n dev

# Aplicar
kubectl apply -f projects/k8s/base/ingress.yaml

# Acessar aplicação

kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/main/deploy/static/provider/kind/deploy.yaml

# Aguardar estar pronto
kubectl wait --namespace ingress-nginx \
--for=condition=ready pod \
--selector=app.kubernetes.io/component=controller \
--timeout=90s

## Liberar porta local
kubectl port-forward service/java-app 8079:8079 -n dev



## Restart
# Rebuild da imagem Docker
docker build -t java-app:latest ./app

# Recriar o deployment
kubectl rollout restart deployment/java-app -n dev

# Acompanhar os logs
kubectl logs -f deployment/java-app -n dev

# Verificar sercrets
kubectl get secrets -n dev

# Deletar pods antigos
kubectl delete pod -l app=challenge-english -n dev

# Aguardar novos pods subirem
kubectl get pods -n dev -w


## PAUSAR TUDO
    kubectl scale deployment java-app --replicas=0 -n dev
    kubectl scale deployment --all --replicas=0 -n dev

## PLAY TUDO
    kubectl scale deployment java-app --replicas=1 -n dev
    kubectl scale deployment --all --replicas=1 -n dev

## DELETAR TUDO
# RAPIDO
    kubectl delete namespace dev
    kubectl apply -f projects/k8s/namespace.yaml
# INDIVIDUAL
    kubectl delete -f projects/k8s/base/challenge-english/ -n dev
    kubectl delete -f projects/k8s/base/go-app/ -n dev
    kubectl delete -f projects/k8s/base/java-app/ -n dev
    kubectl delete -f projects/k8s/base/redis/ -n dev
    kubectl delete -f projects/k8s/base/rabbitmq/ -n dev
    kubectl delete -f projects/k8s/base/postgres/ -n dev

# Deletar ingress
    kubectl delete -f projects/k8s/base/ingress.yaml

# Deletar secrets
    kubectl delete secret api-secret -n dev
    kubectl delete secret challenge-english-secret -n dev

# Deletar namespace
    kubectl delete namespace dev