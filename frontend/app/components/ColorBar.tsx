import colors from "~/domain/Colors";
import {drawingService} from "~/services/DrawingService";

function ColorBar() {
    const onColorClick = (color: string) => {
        drawingService.setColor(color);
    }

    return (
        <div className={"w-1/2 h-full"}>
            <div className={"w-full h-1/2 flex"}>
                {colors.slice(0, colors.length / 2).map((color) => (
                    <div
                        key={color}
                        onClick={() => onColorClick(color)}
                        className={"flex-1 cursor-pointer"}
                        style={{ backgroundColor: color }}
                    >
                    </div>
                ))}
            </div>
            <div className={"w-full h-1/2 flex"}>
                {colors.slice(colors.length / 2).map((color) => (
                    <div
                        key={color}
                        onClick={() => onColorClick(color)}
                        className={"flex-1 cursor-pointer"}
                        style={{ backgroundColor: color }}
                    >
                    </div>
                ))}
            </div>
        </div>
    );
}

export default ColorBar;