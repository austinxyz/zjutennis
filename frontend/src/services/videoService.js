import axios from 'axios';

const API_URL = '/api/videos';

class VideoService {
  /**
   * Get all videos
   * @returns {Promise<Array>} List of all videos
   */
  async getAllVideos() {
    const response = await axios.get(API_URL);
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
   * Get video for a specific match (matches can only have one video)
   * @param {number} matchId - Match ID
   * @returns {Promise<Object>} Video object or null
   */
  async getVideoByMatch(matchId) {
    try {
      const response = await axios.get(`/api/matches/${matchId}/video`);
      return response.data;
    } catch (error) {
      if (error.response && error.response.status === 404) {
        return null;
      }
      throw error;
    }
  }

  /**
   * Create a new video
   * @param {Object} videoData - Video data (description, videoUrl, thumbnailUrl, durationSeconds, matchId, totalShots, errors, winners, aces, doubleFaults, runningDistanceMeters)
   * @returns {Promise<Object>} Created video object
   */
  async createVideo(videoData) {
    const response = await axios.post(API_URL, videoData);
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
   * Delete a video (will cascade delete all analyses)
   * @param {number} id - Video ID
   * @returns {Promise<void>}
   */
  async deleteVideo(id) {
    await axios.delete(`${API_URL}/${id}`);
  }
}

export default new VideoService();
