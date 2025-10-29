import axios from 'axios';

const API_URL = '/api/matches';

class MatchService {
  /**
   * Get all matches
   * @returns {Promise<Array>} List of all matches
   */
  async getAllMatches() {
    const response = await axios.get(API_URL);
    return response.data;
  }

  /**
   * Get match by ID
   * @param {number} id - Match ID
   * @returns {Promise<Object>} Match object
   */
  async getMatchById(id) {
    const response = await axios.get(`${API_URL}/${id}`);
    return response.data;
  }

  /**
   * Get matches by type (singles/doubles)
   * @param {string} matchType - Match type
   * @returns {Promise<Array>} List of matches
   */
  async getMatchesByType(matchType) {
    const response = await axios.get(`${API_URL}/type/${matchType}`);
    return response.data;
  }

  /**
   * Get matches for a specific player
   * @param {number} playerId - Player ID
   * @returns {Promise<Array>} List of matches
   */
  async getPlayerMatches(playerId) {
    const response = await axios.get(`${API_URL}/player/${playerId}`);
    return response.data;
  }

  /**
   * Get recent matches
   * @returns {Promise<Array>} List of recent matches
   */
  async getRecentMatches() {
    const response = await axios.get(`${API_URL}/recent`);
    return response.data;
  }

  /**
   * Get matches won by our team
   * @returns {Promise<Array>} List of won matches
   */
  async getMatchesWonByOurTeam() {
    const response = await axios.get(`${API_URL}/wins`);
    return response.data;
  }

  /**
   * Create a new match
   * @param {Object} matchData - Match data
   * @returns {Promise<Object>} Created match object
   */
  async createMatch(matchData) {
    const response = await axios.post(API_URL, matchData);
    return response.data;
  }

  /**
   * Update an existing match
   * @param {number} id - Match ID
   * @param {Object} matchData - Updated match data
   * @returns {Promise<Object>} Updated match object
   */
  async updateMatch(id, matchData) {
    const response = await axios.put(`${API_URL}/${id}`, matchData);
    return response.data;
  }

  /**
   * Delete a match
   * @param {number} id - Match ID
   * @returns {Promise<void>}
   */
  async deleteMatch(id) {
    await axios.delete(`${API_URL}/${id}`);
  }

  /**
   * Get match statistics
   * @returns {Promise<Object>} Match statistics (totalMatches, singlesMatches, doublesMatches)
   */
  async getMatchStatistics() {
    const response = await axios.get(`${API_URL}/statistics`);
    return response.data;
  }

  /**
   * Attach video to match
   * @param {number} matchId - Match ID
   * @param {Object} videoData - Video data
   * @returns {Promise<Object>} Created video object
   */
  async attachVideo(matchId, videoData) {
    const response = await axios.post(`${API_URL}/${matchId}/video`, videoData);
    return response.data;
  }

  /**
   * Remove video from match
   * @param {number} matchId - Match ID
   * @returns {Promise<void>}
   */
  async removeVideo(matchId) {
    await axios.delete(`${API_URL}/${matchId}/video`);
  }

  /**
   * Get video for match
   * @param {number} matchId - Match ID
   * @returns {Promise<Object>} Video object
   */
  async getMatchVideo(matchId) {
    const response = await axios.get(`${API_URL}/${matchId}/video`);
    return response.data;
  }

  /**
   * Check if match has video
   * @param {number} matchId - Match ID
   * @returns {Promise<boolean>} True if match has video
   */
  async hasVideo(matchId) {
    const response = await axios.get(`${API_URL}/${matchId}/has-video`);
    return response.data.hasVideo;
  }
}

export default new MatchService();
