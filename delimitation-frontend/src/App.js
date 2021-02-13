import logo from './logo.svg';
import './App.css';
import {useEffect, useState} from "react";

const App = () => {
    const [backendResponse, setBackendResponse] = useState()

    useEffect(() => {
        fetch('https://delimitation.ey.r.appspot.com/')
            .then(response => response.json())
            .then(data => setBackendResponse(data));
    }, [])

    return (
        <div className="App">
            <header className="App-header">
                <img src={logo} className="App-logo" alt="logo"/>
                <p>
                    Edit <code>src/App.js</code> and save to reload. {backendResponse?.hello}
                </p>
                <a
                    className="App-link"
                    href="https://reactjs.org"
                    target="_blank"
                    rel="noopener noreferrer"
                >
                    Learn React
                </a>
            </header>
        </div>
    );
};

export default App;
