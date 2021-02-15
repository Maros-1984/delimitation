import {useWindowDimensions} from "../../hooks/useWindowDimensions";

export const Area = ({gameWidth, color, areaX, areaY, setHighlightedMove}) => {
    const {width} = useWindowDimensions()
    const areaWidth = .9 / gameWidth * width;

    const areaStyle = {
        paddingBottom: areaWidth + "px",
        width: areaWidth + "px",
    };

    const handleMouseMove = (e) => {
        let x = e.nativeEvent.offsetX;
        let y = e.nativeEvent.offsetY;

        if (x > y) {
            if (areaWidth - x > y) {
                // Highlight upper border.
                console.log('up')
                setHighlightedMove({areaX, areaY: areaY - 1, bottom: true})
            } else {
                // Highlight right border.
                console.log('right')
                setHighlightedMove({areaX, areaY, right: true})
            }
        } else {
            if (areaWidth - x > y) {
                // Highlight left border.
                console.log('left')
                setHighlightedMove({areaX: areaX - 1, areaY, right: true})
            } else {
                // Highlight bottom border.
                console.log('bottom')
                setHighlightedMove({areaX, areaY, bottom: true})
            }
        }
    };

    return <td onMouseMove={handleMouseMove} className={color.toLowerCase()} style={areaStyle}/>;
};