import axios, {type AxiosInstance} from 'axios';
import { API_CONFIG } from '../config/api';

const apiClient: AxiosInstance = axios.create({
  baseURL: API_CONFIG.FULL_URL,
  timeout: API_CONFIG.TIMEOUT,
  headers: {
    'Content-Type': 'application/json',
  },
});

export default apiClient;