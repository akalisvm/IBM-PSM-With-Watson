import axios from 'axios'
import { getCookie } from "@/utils/cookie.utils";

const request = axios.create({
    baseURL: '/api',
    timeout: 10000
})

request.interceptors.request.use(config => {
    config.headers['Content-Type'] = 'application/json;charset=utf-8';
    if(getCookie("user")) {
        config.headers['token'] = JSON.parse(getCookie("user")).app_token;
    }
    return config
}, error => {
    return Promise.reject(error)
});

request.interceptors.response.use(
    response => {
        let res = response.data;
        if (response.config.responseType === 'blob') {
            return res
        }
        if (typeof res === 'string') {
            res = res ? JSON.parse(res) : res
        }
        return res;
    }, error => {
        console.log('err' + error)
        return Promise.reject(error)
    }
)

export default request