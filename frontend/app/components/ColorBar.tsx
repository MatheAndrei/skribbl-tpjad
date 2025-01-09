import {observer} from "mobx-react-lite";
import {drawingService} from "~/services/DrawingService";
import colors from "~/domain/Colors";

const ColorBar = observer(() => {

    const onColorClick = (color: string) => {
        drawingService.setColor(color);
    }

    return (
        <div className={"w-full h-16 grid grid-rows-2 grid-flow-col gap-2"}>
            {colors.map((color) => (
                color === drawingService.drawingStore.color ? (
                    <div
                        key={color}
                        className={"w-full h-full rounded-sm outline outline-2 outline-foreground cursor-pointer"}
                        style={{backgroundColor: color}}
                        onClick={() => onColorClick(color)}
                    ></div>
                ) : (
                    <div
                        key={color}
                        className={"w-full h-full rounded-sm cursor-pointer"}
                        style={{backgroundColor: color}}
                        onClick={() => onColorClick(color)}
                    ></div>
                )
            ))}
        </div>
    );
});

export default ColorBar;