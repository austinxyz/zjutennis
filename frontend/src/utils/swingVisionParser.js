/**
 * SwingVision CSV Parser
 * Parses SwingVision CSV exports and maps them to VideoAnalysis data model
 */

/**
 * Parse SwingVision CSV file
 * @param {File} file - CSV file from SwingVision
 * @returns {Promise<Object>} Parsed analysis data
 */
export async function parseSwingVisionCSV(file) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader();

    reader.onload = (e) => {
      try {
        const text = e.target.result;
        const data = parseCSVText(text);
        resolve(data);
      } catch (error) {
        reject(error);
      }
    };

    reader.onerror = () => {
      reject(new Error('Failed to read CSV file'));
    };

    reader.readAsText(file);
  });
}

/**
 * Parse CSV text content
 * @param {string} csvText - CSV content as text
 * @returns {Object} Parsed analysis data
 */
function parseCSVText(csvText) {
  const lines = csvText.split('\n').map(line => line.trim()).filter(line => line);

  if (lines.length === 0) {
    throw new Error('CSV file is empty');
  }

  // Parse headers
  const headers = parseCSVLine(lines[0]);

  // Parse data rows
  const rows = [];
  for (let i = 1; i < lines.length; i++) {
    const values = parseCSVLine(lines[i]);
    if (values.length === headers.length) {
      const row = {};
      headers.forEach((header, index) => {
        row[header] = values[index];
      });
      rows.push(row);
    }
  }

  // Map to VideoAnalysis model
  return mapToVideoAnalysis(rows, headers);
}

/**
 * Parse a single CSV line handling quoted values
 * @param {string} line - CSV line
 * @returns {Array<string>} Parsed values
 */
function parseCSVLine(line) {
  const values = [];
  let current = '';
  let inQuotes = false;

  for (let i = 0; i < line.length; i++) {
    const char = line[i];

    if (char === '"') {
      inQuotes = !inQuotes;
    } else if (char === ',' && !inQuotes) {
      values.push(current.trim());
      current = '';
    } else {
      current += char;
    }
  }

  values.push(current.trim());
  return values;
}

/**
 * Map SwingVision data to VideoAnalysis model
 * @param {Array<Object>} rows - Parsed CSV rows
 * @param {Array<string>} headers - CSV headers
 * @returns {Object} VideoAnalysis data
 */
function mapToVideoAnalysis(rows, headers) {
  const analysisData = {
    strengthForehandScore: null,
    strengthServeScore: null,
    strengthVolleyScore: null,
    strengthMovementScore: null,
    strengthSummary: '',
    weaknessBackhandScore: null,
    weaknessConsistencyScore: null,
    weaknessPressureScore: null,
    weaknessSummary: '',
    tacticalStyle: '',
    aggressionIndex: null,
    netApproachFrequency: null,
    tacticalSummary: '',
    aiRecommendations: '',
    trainingFocusAreas: ''
  };

  // Check if this is a SwingVision summary format or detailed stats format
  if (isSwingVisionSummaryFormat(headers)) {
    return parseSwingVisionSummary(rows, analysisData);
  } else if (isSwingVisionDetailedFormat(headers)) {
    return parseSwingVisionDetailed(rows, analysisData);
  } else {
    // Generic CSV parsing - try to match column names
    return parseGenericCSV(rows, headers, analysisData);
  }
}

/**
 * Check if CSV is SwingVision summary format
 */
function isSwingVisionSummaryFormat(headers) {
  const summaryHeaders = ['metric', 'value', 'rating'];
  return summaryHeaders.every(h =>
    headers.some(header => header.toLowerCase().includes(h))
  );
}

/**
 * Check if CSV is SwingVision detailed stats format
 */
function isSwingVisionDetailedFormat(headers) {
  const detailedHeaders = ['shot type', 'count', 'winners', 'errors'];
  return detailedHeaders.some(h =>
    headers.some(header => header.toLowerCase().includes(h))
  );
}

/**
 * Parse SwingVision summary format CSV
 */
function parseSwingVisionSummary(rows, analysisData) {
  const metrics = {};

  rows.forEach(row => {
    const metric = row.metric || row.Metric || '';
    const value = row.value || row.Value || '';
    const rating = row.rating || row.Rating || '';

    metrics[metric.toLowerCase()] = { value, rating };
  });

  // Map metrics to analysis data
  if (metrics['forehand']) {
    analysisData.strengthForehandScore = parseRating(metrics['forehand'].rating);
  }
  if (metrics['serve']) {
    analysisData.strengthServeScore = parseRating(metrics['serve'].rating);
  }
  if (metrics['volley']) {
    analysisData.strengthVolleyScore = parseRating(metrics['volley'].rating);
  }
  if (metrics['movement'] || metrics['footwork']) {
    const movement = metrics['movement'] || metrics['footwork'];
    analysisData.strengthMovementScore = parseRating(movement.rating);
  }
  if (metrics['backhand']) {
    analysisData.weaknessBackhandScore = 10 - parseRating(metrics['backhand'].rating);
  }
  if (metrics['consistency']) {
    analysisData.weaknessConsistencyScore = 10 - parseRating(metrics['consistency'].rating);
  }
  if (metrics['aggression'] || metrics['aggression index']) {
    const aggression = metrics['aggression'] || metrics['aggression index'];
    analysisData.aggressionIndex = parseFloat(aggression.value) || null;
  }
  if (metrics['net approach'] || metrics['net play']) {
    const netPlay = metrics['net approach'] || metrics['net play'];
    analysisData.netApproachFrequency = parseFloat(netPlay.value) || null;
  }

  return analysisData;
}

/**
 * Parse SwingVision detailed stats format
 */
function parseSwingVisionDetailed(rows, analysisData) {
  let forehandWinners = 0, forehandErrors = 0, forehandTotal = 0;
  let backhandWinners = 0, backhandErrors = 0, backhandTotal = 0;
  let serveWinners = 0, serveErrors = 0, serveTotal = 0;
  let volleyWinners = 0, volleyErrors = 0, volleyTotal = 0;
  let netApproaches = 0;

  rows.forEach(row => {
    const shotType = (row['shot type'] || row['Shot Type'] || '').toLowerCase();
    const count = parseInt(row.count || row.Count || 0);
    const winners = parseInt(row.winners || row.Winners || 0);
    const errors = parseInt(row.errors || row.Errors || 0);

    if (shotType.includes('forehand')) {
      forehandWinners += winners;
      forehandErrors += errors;
      forehandTotal += count;
    } else if (shotType.includes('backhand')) {
      backhandWinners += winners;
      backhandErrors += errors;
      backhandTotal += count;
    } else if (shotType.includes('serve')) {
      serveWinners += winners;
      serveErrors += errors;
      serveTotal += count;
    } else if (shotType.includes('volley')) {
      volleyWinners += winners;
      volleyErrors += errors;
      volleyTotal += count;
    }

    if (shotType.includes('net') || shotType.includes('volley')) {
      netApproaches += count;
    }
  });

  // Calculate scores based on winner/error ratios
  if (forehandTotal > 0) {
    const forehandRatio = (forehandWinners - forehandErrors) / forehandTotal;
    analysisData.strengthForehandScore = Math.max(0, Math.min(10, 5 + forehandRatio * 10));
  }

  if (backhandTotal > 0) {
    const backhandRatio = (backhandErrors - backhandWinners) / backhandTotal;
    analysisData.weaknessBackhandScore = Math.max(0, Math.min(10, 5 + backhandRatio * 10));
  }

  if (serveTotal > 0) {
    const serveRatio = (serveWinners - serveErrors) / serveTotal;
    analysisData.strengthServeScore = Math.max(0, Math.min(10, 5 + serveRatio * 10));
  }

  if (volleyTotal > 0) {
    const volleyRatio = (volleyWinners - volleyErrors) / volleyTotal;
    analysisData.strengthVolleyScore = Math.max(0, Math.min(10, 5 + volleyRatio * 10));
  }

  const totalShots = forehandTotal + backhandTotal + serveTotal + volleyTotal;
  if (totalShots > 0) {
    analysisData.netApproachFrequency = (netApproaches / totalShots) * 100;
  }

  return analysisData;
}

/**
 * Parse generic CSV format - try to match column names intelligently
 */
function parseGenericCSV(rows, headers, analysisData) {
  // Try to find and parse relevant columns
  if (rows.length === 0) return analysisData;

  const firstRow = rows[0];

  // Look for specific field mappings
  Object.keys(firstRow).forEach(key => {
    const lowerKey = key.toLowerCase();
    const value = firstRow[key];

    // Match strength scores
    if (lowerKey.includes('forehand') && lowerKey.includes('score')) {
      analysisData.strengthForehandScore = parseFloat(value) || null;
    } else if (lowerKey.includes('serve') && lowerKey.includes('score')) {
      analysisData.strengthServeScore = parseFloat(value) || null;
    } else if (lowerKey.includes('volley') && lowerKey.includes('score')) {
      analysisData.strengthVolleyScore = parseFloat(value) || null;
    } else if (lowerKey.includes('movement') && lowerKey.includes('score')) {
      analysisData.strengthMovementScore = parseFloat(value) || null;
    }
    // Match weakness scores
    else if (lowerKey.includes('backhand') && lowerKey.includes('score')) {
      analysisData.weaknessBackhandScore = parseFloat(value) || null;
    } else if (lowerKey.includes('consistency') && lowerKey.includes('score')) {
      analysisData.weaknessConsistencyScore = parseFloat(value) || null;
    } else if (lowerKey.includes('pressure') && lowerKey.includes('score')) {
      analysisData.weaknessPressureScore = parseFloat(value) || null;
    }
    // Match tactical data
    else if (lowerKey.includes('style') || lowerKey === 'playing style') {
      analysisData.tacticalStyle = value;
    } else if (lowerKey.includes('aggression')) {
      analysisData.aggressionIndex = parseFloat(value) || null;
    } else if (lowerKey.includes('net') && lowerKey.includes('approach')) {
      analysisData.netApproachFrequency = parseFloat(value) || null;
    }
    // Match summaries
    else if (lowerKey.includes('strength') && lowerKey.includes('summary')) {
      analysisData.strengthSummary = value;
    } else if (lowerKey.includes('weakness') && lowerKey.includes('summary')) {
      analysisData.weaknessSummary = value;
    } else if (lowerKey.includes('tactical') && lowerKey.includes('summary')) {
      analysisData.tacticalSummary = value;
    } else if (lowerKey.includes('recommendation')) {
      analysisData.aiRecommendations = value;
    } else if (lowerKey.includes('training') && lowerKey.includes('focus')) {
      analysisData.trainingFocusAreas = value;
    }
  });

  return analysisData;
}

/**
 * Parse rating string to numeric value (0-10)
 * @param {string} rating - Rating string (e.g., "8/10", "Good", "80%")
 * @returns {number|null} Numeric rating
 */
function parseRating(rating) {
  if (!rating) return null;

  rating = rating.toString().trim();

  // Handle "8/10" format
  if (rating.includes('/')) {
    const parts = rating.split('/');
    return parseFloat(parts[0]) || null;
  }

  // Handle percentage format "80%"
  if (rating.includes('%')) {
    const percent = parseFloat(rating.replace('%', ''));
    return percent ? (percent / 10) : null;
  }

  // Handle text ratings
  const textRatings = {
    'excellent': 9.0,
    'very good': 8.0,
    'good': 7.0,
    'above average': 6.5,
    'average': 5.0,
    'below average': 4.0,
    'poor': 3.0,
    'very poor': 2.0
  };

  const lowerRating = rating.toLowerCase();
  if (textRatings[lowerRating]) {
    return textRatings[lowerRating];
  }

  // Try to parse as number
  const num = parseFloat(rating);
  return isNaN(num) ? null : num;
}

export default {
  parseSwingVisionCSV
};
