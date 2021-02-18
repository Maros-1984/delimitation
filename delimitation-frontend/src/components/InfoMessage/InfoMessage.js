import './InfoMessage.css';

export const InfoMessage = ({game}) => {
    if (!game) return null

    if (game.over) {
        const blue = game.score['BLUE']
        const red = game.score['RED']

        return (
            <div className="infoMessage">
                <h1>Game Over</h1>
                {blue > red && (
                    <h2><span style={{color: 'blue'}}>BLUE</span> player won <span
                        style={{color: 'blue'}}>{blue}</span> tiles to <span
                        style={{color: 'red'}}>RED</span>'s <span style={{color: 'red'}}>{red}</span> tiles.</h2>)}
                {blue < red && (
                    <h2><span style={{color: 'red'}}>RED</span> player won <span
                        style={{color: 'red'}}>{red}</span> tiles to <span
                        style={{color: 'blue'}}>BLUE</span>'s <span style={{color: 'blue'}}>{blue}</span> tiles.</h2>)}
                {blue === red && (
                    <h2>It's a draw. <span style={{color: 'blue'}}>BLUE</span> player has <span
                        style={{color: 'blueviolet'}}>{blue}</span> tiles as well as <span
                        style={{color: 'red'}}>RED</span> player.</h2>)}
            </div>);
    }

    if (game.possibleMoves.length === 0) {
        return (
            <div className="infoMessage">
                <h1 className="glow">Wait for the <span
                    style={{color: game.playerOnMove.toLowerCase()}}>other player</span> to move.</h1>
                {game.moves.length <= 1 && <p>Send him the URL if you have not already.</p>}
            </div>);
    }

    if (game.moves.length === 0) {
        return <div className="infoMessage"><h1>Make your first move on the corners.</h1></div>;
    }

    return null
}