import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { Line } from 'react-chartjs-2';
import { Chart as ChartJS, CategoryScale, LinearScale, PointElement, LineElement, Title, Tooltip, Legend, TimeScale } from 'chart.js';
import 'chartjs-adapter-date-fns';


// Register necessary components
ChartJS.register(CategoryScale, LinearScale, PointElement, LineElement, Title, Tooltip, Legend, TimeScale);

const PlayerCounts = () => {
    const { id } = useParams();
    const [playerCounts, setPlayerCounts] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {

        console.log("Global baseURL is:", global.baseURL);
        const fetchGameDetails = async () => {
            try {
                const response = await fetch(global.baseURL + `/api/v1/games/${id}/records`);
                if (!response.ok) {
                    throw new Error("Failed to fetch game details");
                }
                const data = await response.json();
                setPlayerCounts(data);
                setLoading(false);
            } catch (error) {
                setError(error.message);
                setLoading(false);
            }
        };

        fetchGameDetails();
    }, [id]);

    if (loading) return <p>Loading game details...</p>;
    if (error) return <p>Error: {error}</p>;

    // Prepare data for the chart
    const chartData = {
        labels: playerCounts.map(entry => entry.recordedAt),
        datasets: [
            {
                label: 'Player Count',
                data: playerCounts.map(entry => entry.playerCount),
                borderColor: 'rgba(75,192,192,1)',
                backgroundColor: 'rgba(75,192,192,0.2)',
                fill: true,
                tension: 0.1
            }
        ]
    };

    const chartOptions = {
        responsive: true,
        plugins: {
            title: {
                display: true,
                text: 'Player Count Over Time'
            },
            tooltip: {
                callbacks: {
                    label: (context) => {
                        // Customize tooltip to show date and time
                        return `Player Count: ${context.raw}`;
                    }
                }
            }
        },
        scales: {
            x: {
                type: 'time',
                time: {
                    unit: 'day', // Group x-axis labels by day
                    displayFormats: {
                        day: 'MMM d'  // Show only the date (e.g., "Oct 5")
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
            <h2>Game Details</h2>
            {playerCounts ? (
                <div>
                    <div style={{ width: '100%', height: '400px' }}>
                        <Line data={chartData} options={chartOptions} />
                    </div>
                </div>
            ) : (
                <p>Game data not found.</p>
            )}
        </div>
    );
};

export default PlayerCounts;
