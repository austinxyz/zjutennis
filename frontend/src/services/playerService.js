import axios from 'axios';

const API_URL = '/api/players';

class PlayerService {
  // Player basic information
  async getAllPlayers() {
    const response = await axios.get(API_URL);
    return response.data;
  }

  async searchPlayers(searchRequest) {
    const response = await axios.post(`${API_URL}/search`, searchRequest);
    return response.data;
  }

  async getPlayerById(id) {
    const response = await axios.get(`${API_URL}/${id}`);
    return response.data;
  }

  async createPlayer(player) {
    const response = await axios.post(API_URL, player);
    return response.data;
  }

  async updatePlayer(id, player) {
    const response = await axios.put(`${API_URL}/${id}`, player);
    return response.data;
  }

  // Import players from CSV
  async importPlayersFromCSV(file) {
    const formData = new FormData();
    formData.append('file', file);
    const response = await axios.post(`${API_URL}/import`, formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    });
    return response.data;
  }
}

export default new PlayerService();
