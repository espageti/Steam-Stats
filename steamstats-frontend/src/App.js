import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import GamesList from './components/GamesList';
import GameDetails from './components/GameDetails';
import PlayerCounts from './components/PlayerCounts';
import CompareGames from "./components/CompareGames";
import CompareCounts from './components/CompareCounts';

global.baseURL = "https://steamstats-backend-d2f05f06780a.herokuapp.com"

function App() {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<GamesList />} />
                <Route 
                    path="/games/:id" 
                    element={
                        <div>
                            <GameDetails /> 
                            <PlayerCounts /> 
                        </div>
                    } 
                />
                <Route 
                    path="compare/:ids"
                    element={
                        <div>
                            <CompareGames/>
                            <CompareCounts/>
                        </div>
                    }
                />
            </Routes>
        </Router>
    );
}

export default App;
