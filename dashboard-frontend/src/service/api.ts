import {apiClient, mlClient} from './axios';

export const userAPI = {
  login: async (credentials: { username: string; password: string }) => {
    const response = await apiClient.post('/auth/login', credentials);
    return response.data;
  },

  getHeatMapData: async () => {
    const response = await apiClient.get('/simulation/heatmap');
    return response.data;
  }
  
};