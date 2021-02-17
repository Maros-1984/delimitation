import './InfoMessage.css';

export const InfoMessage = ({game}) => {
    if (!game) return null

    if (game.possibleMoves.length === 0) {
        return (
            <div className="infoMessage">
                <h1 className="glow">Wait for <span
                    style={{color: game.playerOnMove.toLowerCase()}}>other player</span> to move.</h1>
                {game.moves.length <= 1 && <p>Send him the URL if you have not already.</p>}
            </div>);
    }

    if (game.moves.length === 0) {
        return <div className="infoMessage"><h1>Make your first move on the corners.</h1></div>;
    }

    if (game.over) {
        const blue = game.score['BLUE']
        const red = game.score['RED']

        return (
            <div className="infoMessage">
                <h1>Game Over</h1>
                {blue > red && (
                    <h2><span style={{color: 'blue'}}>BLUE</span> player won {blue} tiles to <span
                        style={{color: 'red'}}>RED</span>'s {red} tiles.</h2>)}
                {blue < red && (
                    <h2><span style={{color: 'red'}}>RED</span> player won {red} tiles to <span
                        style={{color: 'blue'}}>BLUE</span>'s {blue} tiles.</h2>)}
                {blue === red && (
                    <h2>It's a draw. <span style={{color: 'blue'}}>BLUE</span> player has {blue} tiles as well as <span
                        style={{color: 'red'}}>RED</span> player.</h2>)}
            </div>);
    }

    return null
}