import React, { useEffect, useState } from 'react';
import { getGamesByPage } from '../constants/apiService'; // Updated service for paginated API
import { Link } from 'react-router-dom';

const GamesList = () => {
  const [games, setGames] = useState([]); // Initialize as an empty array
  const [currentPage, setCurrentPage] = useState(1);

  useEffect(() => {
    // Fetch games for the current page
    const fetchGames = async () => {
      try {
        const data = await getGamesByPage(currentPage, 100); // Provide default values in case of undefined
        setGames(data); // Ensure data is always an array
      } catch (error) {
        console.error('Failed to fetch games:', error); // Log any errors
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
    <div style={{ textAlign: 'center' }}>
      <h2>Games List</h2>

      {/* Games Table */}
      <div style={{ display: 'inline-block' }}>
        <table>
          <thead>
            <tr>
              <th>App ID</th>
              <th>Title</th>
              <th>Average Player Count</th>
            </tr>
          </thead>
          <tbody>
            {games && games.length > 0 ? (
              games.map(game => (
                <tr key={game.appId}>
                  <td>{game.appId}</td>
                  <td style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
                    <img 
                      src={game.headerImage} 
                      alt={game.title} 
                      style={{ width: '120px', height: '60px', objectFit: 'cover' }} 
                    />
                    <Link to={`/games/${game.appId}`}>{game.title}</Link>
                  </td>
                  <td>{game.averagePlayerCount ? game.averagePlayerCount.toLocaleString() : 'N/A'}</td>
                </tr>
              ))
            ) : (
              <tr>
                <td colSpan="3">Loading...</td>
              </tr>
            )}
          </tbody>
        </table>
      </div>

      {/* Pagination Controls */}
      <div style={{ marginTop: '20px' }}>
        <button onClick={handlePreviousPage} disabled={currentPage === 1}>
          Previous
        </button>
        <span style={{ margin: '0 10px' }}>Page {currentPage}</span>
        <button onClick={handleNextPage} disabled={games.length < 10}>
          Next
        </button>
      </div>
    </div>
  );
};

export default GamesList;
