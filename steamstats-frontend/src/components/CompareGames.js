import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';

const CompareGames = () => {
    const { ids } = useParams(); // Get the game IDs from the URL parameters
    const gameIds = React.useMemo(() => ids.split('&'), [ids]);
    const [games, setGames] = useState({}); // State to hold game details as a dictionary
    const [loading, setLoading] = useState(true); // Loading state
    const [error, setError] = useState(null); // Error state

    useEffect(() => {
        // Fetch game details based on the IDs
        const fetchGameDetails = async () => {
            try {
                const gamesDict = {};
                for (const id of gameIds) {
                    const response = await fetch(global.baseURL + `/api/v1/games/${id}`);
                    if (!response.ok) {
                        throw new Error(`Failed to fetch game details for ID: ${id}`);
                    }
                    const data = await response.json();
                    gamesDict[id] = data; // Add game details to the dictionary
                }
                setGames(gamesDict); // Set the dictionary to state
                setLoading(false); // Set loading to false after data is fetched
            } catch (error) {
                setError(error.message);
                setLoading(false);
            }
        };

        fetchGameDetails();
    }, [gameIds]); // Re-run the effect if the IDs change

    if (loading) {
        return <div>Loading...</div>;
    }

    if (error) {
        return <div>Error: {error}</div>;
    }

    return (
        <div>
            <h1>Game Comparison</h1>
            <ul>
                {Object.entries(games).map(([id, game]) => (
                    <li key={id}>
                        <h2>{game.title} (ID: {id})</h2>
                        <p>{game.developer}</p>
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default CompareGames;
