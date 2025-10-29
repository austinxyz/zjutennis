import axios from 'axios';

const API_URL = '/api/video-analyses';

class VideoAnalysisService {
  /**
   * Get all video analyses
   * @returns {Promise<Array>} List of all analyses
   */
  async getAllAnalyses() {
    const response = await axios.get(API_URL);
    return response.data;
  }

  /**
   * Get analysis by ID
   * @param {number} id - Analysis ID
   * @returns {Promise<Object>} Analysis object
   */
  async getAnalysisById(id) {
    const response = await axios.get(`${API_URL}/${id}`);
    return response.data;
  }

  /**
   * Get all analyses for a video
   * @param {number} videoId - Video ID
   * @returns {Promise<Array>} List of analyses
   */
  async getAnalysesByVideo(videoId) {
    const response = await axios.get(`${API_URL}/video/${videoId}`);
    return response.data;
  }

  /**
   * Get all analyses for a player
   * @param {number} playerId - Player ID
   * @returns {Promise<Array>} List of analyses
   */
  async getAnalysesByPlayer(playerId) {
    const response = await axios.get(`${API_URL}/player/${playerId}`);
    return response.data;
  }

  /**
   * Get analysis for specific video and player
   * @param {number} videoId - Video ID
   * @param {number} playerId - Player ID
   * @returns {Promise<Object>} Analysis object
   */
  async getAnalysisByVideoAndPlayer(videoId, playerId) {
    const response = await axios.get(`${API_URL}/video/${videoId}/player/${playerId}`);
    return response.data;
  }

  /**
   * Create video analysis
   * @param {Object} analysisData - Analysis data (videoId, playerId, and analysis fields)
   * @returns {Promise<Object>} Created analysis object
   */
  async createAnalysis(analysisData) {
    const response = await axios.post(API_URL, analysisData);
    return response.data;
  }

  /**
   * Update video analysis
   * @param {number} id - Analysis ID
   * @param {Object} analysisData - Updated analysis data
   * @returns {Promise<Object>} Updated analysis object
   */
  async updateAnalysis(id, analysisData) {
    const response = await axios.put(`${API_URL}/${id}`, analysisData);
    return response.data;
  }

  /**
   * Delete video analysis
   * @param {number} id - Analysis ID
   * @returns {Promise<void>}
   */
  async deleteAnalysis(id) {
    await axios.delete(`${API_URL}/${id}`);
  }

  /**
   * Get analysis count for a video
   * @param {number} videoId - Video ID
   * @returns {Promise<number>} Count of analyses
   */
  async getAnalysisCountForVideo(videoId) {
    const response = await axios.get(`${API_URL}/video/${videoId}/count`);
    return response.data;
  }
}

export default new VideoAnalysisService();
