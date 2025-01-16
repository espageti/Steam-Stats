import React, { useEffect, useState } from 'react';
import { getGamesByPage } from '../constants/apiService'; // Updated service for paginated API
import { Link } from 'react-router-dom';

const GamesList = () => {
  const [games, setGames] = useState([]);  // Initialize as an empty array
  const [currentPage, setCurrentPage] = useState(1);

  useEffect(() => {
    
    // Fetch games for the current page
    const fetchGames = async () => {
      try {
        const data = await getGamesByPage(currentPage, 100); // Provide default values in case of undefined
        setGames(data);  // Ensure data is always an array
      } catch (error) {
        console.error("Failed to fetch games:", error);  // Log any errors
      }
    };

    fetchGames();
  }, [currentPage]);

  const handleNextPage = () => {
    setCurrentPage(prev => prev + 1);
  };

  const handlePreviousPage = () => {
    setCurrentPage(prev => prev - 1);
  };

  return (
    <div>
      <h2>Games List</h2>
      <ul>
        {games && games.length > 0 ? (
          games.map(game => (
            <li key={game.appId}>
              <Link to={`/games/${game.appId}`}>{game.title +": " + game.averagePlayerCount.toFixed(2)}</Link>
            </li>
          ))
        ) : (
          <p>No games available.</p>  // Fallback message when no games are present
        )}
      </ul>

      {/* Pagination Controls */}
      <div>
        <button onClick={handlePreviousPage} disabled={currentPage === 1}>
          Previous
        </button>
        <span>Page {currentPage}</span>
        <button onClick={handleNextPage} disabled={games.length < 10}>
          Next
        </button>
      </div>
    </div>
  );
};

export default GamesList;
