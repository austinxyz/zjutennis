import axios from 'axios';

const API_URL = '/api/video-analysis';

class VideoAnalysisService {
  /**
   * Get all videos across all players
   * @returns {Promise<Array>} List of all videos
   */
  async getAllVideos() {
    const response = await axios.get(API_URL);
    return response.data;
  }

  /**
   * Get all videos for a specific player
   * @param {number} playerId - Player ID
   * @returns {Promise<Array>} List of videos
   */
  async getPlayerVideos(playerId) {
    const response = await axios.get(`${API_URL}/player/${playerId}`);
    return response.data;
  }

  /**
   * Get a specific video by ID
   * @param {number} id - Video ID
   * @returns {Promise<Object>} Video object
   */
  async getVideoById(id) {
    const response = await axios.get(`${API_URL}/${id}`);
    return response.data;
  }

  /**
   * Create a new video analysis entry
   * @param {number} playerId - Player ID
   * @param {Object} videoData - Video data
   * @returns {Promise<Object>} Created video object
   */
  async createVideo(playerId, videoData) {
    const response = await axios.post(`${API_URL}/player/${playerId}`, videoData);
    return response.data;
  }

  /**
   * Update an existing video
   * @param {number} id - Video ID
   * @param {Object} videoData - Updated video data
   * @returns {Promise<Object>} Updated video object
   */
  async updateVideo(id, videoData) {
    const response = await axios.put(`${API_URL}/${id}`, videoData);
    return response.data;
  }

  /**
   * Delete a video
   * @param {number} id - Video ID
   * @returns {Promise<void>}
   */
  async deleteVideo(id) {
    await axios.delete(`${API_URL}/${id}`);
  }

  /**
   * Get all analyzed videos for a player
   * @param {number} playerId - Player ID
   * @returns {Promise<Array>} List of analyzed videos
   */
  async getAnalyzedVideos(playerId) {
    const response = await axios.get(`${API_URL}/player/${playerId}/analyzed`);
    return response.data;
  }

  /**
   * Get video statistics for a player
   * @param {number} playerId - Player ID
   * @returns {Promise<Object>} Video statistics (totalVideos, analyzedVideos)
   */
  async getPlayerVideoStats(playerId) {
    const response = await axios.get(`${API_URL}/player/${playerId}/stats`);
    return response.data;
  }

  /**
   * Mark a video as analyzed
   * @param {number} id - Video ID
   * @returns {Promise<Object>} Updated video object
   */
  async markAsAnalyzed(id) {
    const response = await axios.post(`${API_URL}/${id}/mark-analyzed`);
    return response.data;
  }

  /**
   * Update AI analysis results for a video
   * @param {number} id - Video ID
   * @param {Object} aiResults - AI analysis results
   * @returns {Promise<Object>} Updated video object
   */
  async updateAIAnalysis(id, aiResults) {
    const response = await axios.put(`${API_URL}/${id}/ai-analysis`, aiResults);
    return response.data;
  }

  /**
   * Trigger AI analysis for a video
   * @param {number} id - Video ID
   * @returns {Promise<Object>} Analyzed video object
   */
  async analyzeVideo(id) {
    const response = await axios.post(`${API_URL}/${id}/analyze`);
    return response.data;
  }
}

export default new VideoAnalysisService();
