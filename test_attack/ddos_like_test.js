import http from 'k6/http';
import { check, sleep } from 'k6';
import { Trend, Rate } from 'k6/metrics';

const reqDuration = new Trend('req_duration_ms');
const errorRate = new Rate('error_rate');


const BASE_URL = __ENV.K6_TARGET || 'http://localhost:3000';
const AUTH_USER = __ENV.K6_USER || '';
const AUTH_PASS = __ENV.K6_PASS || '';
const RPS_CREATE = Number(__ENV.RPS_CREATE || 500); // req/s para criar recurso
const RPS_READ = Number(__ENV.RPS_READ || 1000);    // req/s para leitura
const DURATION = __ENV.K6_DURATION || '1m';        // duração total do teste

export let options = {
    scenarios: {
        create_flow: {
            executor: 'constant-arrival-rate',
            rate: RPS_CREATE,
            timeUnit: '1s',
            duration: DURATION,
            preAllocatedVUs: 200,
            maxVUs: 1000,
        },

        read_flow: {
            executor: 'constant-arrival-rate',
            rate: RPS_READ,
            timeUnit: '1s',
            duration: DURATION,
            preAllocatedVUs: 200,
            maxVUs: 1000,
        },
    },
    thresholds: {
        'http_req_failed': ['rate<0.05'],          // menos de 5% erros
        'http_req_duration': ['p(95)<1500'],       // p95 abaixo de 1.5s (ajuste)
        'error_rate': ['rate<0.1'],
    },
};

export function setup() {
    if (!AUTH_USER || !AUTH_PASS) {
        return { token: '' };
    }

    const res = http.post(`${BASE_URL}/auth/login`, JSON.stringify({
        username: AUTH_USER,
        password: AUTH_PASS,
    }), {
        headers: { 'Content-Type': 'application/json' },
    });

    if (res.status !== 200) {
        console.warn('setup() login falhou, status:', res.status);
        return { token: '' };
    }

    try {
        const token = res.json().accessToken || res.json().token || '';
        return { token };
    } catch (e) {
        console.warn('setup(): não foi possível parsear token');
        return { token: '' };
    }
}

/* FUNÇÕES helpers */
function createPayload() {
    return JSON.stringify({
        title: `task-${Math.random().toString(36).slice(2, 8)}`,
        description: 'payload de teste para simular processamento pesado',
        heavyCalc: Array(50).fill(0).map(() => Math.random()), // simula payload maior
        ts: Date.now()
    });
}
export default function (data, params) {
    const token = data.token || '';
    const commonHeaders = {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        /*'x-test-run': 'ddos_like_k6'*/
    };

    const scenario = __VU ? __ENV.K6_SCENARIO : undefined;
    /*const possibility = Math.random();

    if (possibility < 0.45) {
        // POST heavy (cria recurso) — objetivo: forçar CPU, banco, escrita
        const res = http.post(`${BASE_URL}/api/tasks`, createPayload(), {
            headers: {
                ...commonHeaders,
                ...(token ? { Authorization: `Bearer ${token}` } : {})
            },
            tags: { action: 'create' }
        });

        reqDuration.add(res.timings.duration);
        errorRate.add(res.status >= 400);

        check(res, {
            'create status 201 or 200': r => r.status === 201 || r.status === 200,
            'create latency < 2000ms': r => r.timings.duration < 2000,
        });

    }*/
    /*if(possibility >= 0.45) {
    */    const id = Math.floor(Math.random() * 1000);
        const res = http.get(`${BASE_URL}/products`, {
            headers: {
                ...commonHeaders,
            }
        });

        reqDuration.add(res.timings.duration);
        errorRate.add(res.status >= 400);

        check(res, {
            'read status 200 or 404': r => r.status === 200 || r.status === 404,
            'read latency < 1500ms': r => r.timings.duration < 1500,
        });
    /*}*/

    // sem sleep: maximiza RPS. Se quiser reduzir, descomente abaixo.
    /*sleep(1);*/
}
