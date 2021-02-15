export const Area = ({gameWidth, color}) => {
    const areaStyle = {
        paddingBottom: gameWidth,
        width: gameWidth,
    };

    return <td className={color.toLowerCase()} style={areaStyle}/>;
};