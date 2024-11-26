export const getGamesByPage = async (page, limit) => {
  try {
    const response = await fetch(`http://localhost:8080/api/v1/games/popular?page=${page}&limit=${limit}`);
    
    if (!response.ok) {
      throw new Error('Failed to fetch games');
    }

    const data = await response.json();
    
    // Log the response to see its structure
    console.log("Fetched games data:", data);
    
    // Ensure the API returns data in the expected structure
    return  data 
  } catch (error) {
    console.error("Error fetching games:", error);
    throw error;  // Re-throw the error to be handled by the caller
  }
};
