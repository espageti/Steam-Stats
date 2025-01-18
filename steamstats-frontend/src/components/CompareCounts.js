import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { Line } from 'react-chartjs-2';
import { Chart as ChartJS, CategoryScale, LinearScale, PointElement, LineElement, Title, Tooltip, Legend, TimeScale } from 'chart.js';
import 'chartjs-adapter-date-fns';

// Register necessary components
ChartJS.register(CategoryScale, LinearScale, PointElement, LineElement, Title, Tooltip, Legend, TimeScale);

const CompareCounts = () => {
    const { ids } = useParams(); // Fetch multiple game IDs (e.g., "1&2&3")
    const gameIds = React.useMemo(() => ids.split('&'), [ids]);
    const [playerCounts, setPlayerCounts] = useState({});
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchGameDetails = async () => {
            try {
                const dataByGame = {};

                for (const id of gameIds) {
                    const response = await fetch(global.baseURL + `/api/v1/games/${id}/records`);
                    if (!response.ok) {
                        throw new Error(`Failed to fetch player counts for game ID: ${id}`);
                    }
                    const data = await response.json();
                    dataByGame[id] = data;
                }

                setPlayerCounts(dataByGame); // Store data for all games
                setLoading(false);
            } catch (error) {
                setError(error.message);
                setLoading(false);
            }
        };

        fetchGameDetails();
    }, [gameIds]);

    if (loading) return <p>Loading game details...</p>;
    if (error) return <p>Error: {error}</p>;

    // Prepare datasets for the chart
    const datasets = Object.entries(playerCounts).map(([gameId, records], index) => ({
        label: `Game ${gameId}`,
        data: records.map(entry => ({ x: entry.recordedAt, y: entry.playerCount })),
        borderColor: `hsl(${index * 60}, 70%, 50%)`, // Generate a unique color for each game
        backgroundColor: `hsl(${index * 60}, 70%, 90%)`,
        fill: false,
        tension: 0.1
    }));

    const chartData = {
        datasets
    };

    const chartOptions = {
        responsive: true,
        plugins: {
            title: {
                display: true,
                text: 'Player Count Over Time for Multiple Games'
            },
            tooltip: {
                callbacks: {
                    label: (context) => `Player Count: ${context.raw.y}`
                }
            }
        },
        scales: {
            x: {
                type: 'time',
                time: {
                    unit: 'day',
                    displayFormats: {
                        day: 'MMM d'
                    }
                },
                title: {
                    display: true,
                    text: 'Date'
                }
            },
            y: {
                title: {
                    display: true,
                    text: 'Player Count'
                },
                beginAtZero: true
            }
        }
    };

    return (
        <div>
            <h2>Player Counts Comparison</h2>
            <div style={{ width: '100%', height: '400px' }}>
                <Line data={chartData} options={chartOptions} />
            </div>
        </div>
    );
};

export default CompareCounts;
