import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';


const GameDetails = () => {
    const { id } = useParams(); // Get the game ID from the URL parameters
    const [game, setGame] = useState(null); // State to hold game details
    const [loading, setLoading] = useState(true); // Loading state
    const [error, setError] = useState(null); // Error state

    useEffect(() => {
        // Fetch game details based on the ID
        const fetchGameDetails = async () => {
            try {
                const response = await fetch(global.baseURL + `/api/v1/games/${id}`);
                if (!response.ok) {
                    throw new Error("Failed to fetch game details");
                }
                const data = await response.json();
                setGame(data); // Set the game details to state
                setLoading(false); // Set loading to false after data is fetched
            } catch (error) {
                setError(error.message);
                setLoading(false);
            }
        };

        fetchGameDetails();
    }, [id]); // Re-run the effect if the ID changes

    if (loading) return <p>Loading game details...</p>;
    if (error) return <p>Error: {error}</p>;

    return (
        <div>
            <h2>Game Details</h2>
            {game ? (
                <div>
                    <h3>{game.title}</h3>
                    <p><strong>Developer:</strong> {game.developer}</p>
                    <p><strong>Release Date:</strong> {game.releaseDate}</p>
                </div>
            ) : (
                <p>Game not found.</p>
            )}
        </div>
    );
};

export default GameDetails;
