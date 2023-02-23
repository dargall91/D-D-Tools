import { LoginRegisterRequest } from "@/models/LoginRegisterRequest";
import { User } from "@/models/User";
import { AxiosResponse } from "axios";
import axiosInstance from "./axiosInstance";

const responseBody = <T>(response: AxiosResponse<T>) => response.data;

const requests = {
    get: <T>(url: string) => axiosInstance.get<T>(url).then(responseBody),
    post: <T>(url: string, body: any) => axiosInstance.post<T>(url, body).then(responseBody),
    put: <T>(url: string, body: any) => axiosInstance.put<T>(url, body).then(responseBody)
};

const user = {
    login(user: LoginRegisterRequest) {
        return requests.post<User>('user/login', user);
    },
    register(user: LoginRegisterRequest) {
        return requests.put<User>('user/register', user);
    }
};

const agent = {
    user
};

export default agent;