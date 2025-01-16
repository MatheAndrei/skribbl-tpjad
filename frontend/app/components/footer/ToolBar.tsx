import {useEffect} from "react";
import {observer} from "mobx-react-lite";
import {Button, Slider} from "@nextui-org/react";
import {DrawingService, drawingService} from "~/services/DrawingService";
import ToolEnum from "~/services/tools/ToolEnum";

const ToolBar = observer(() => {

    useEffect(() => {
        window.addEventListener("keydown", onKeyPressed);

        return () => {
            window.removeEventListener("keydown", onKeyPressed);
        }
    }, []);

    const onToolSelect = (tool: ToolEnum) => {
        drawingService.setTool(tool);
    };

    const onClear = () => {
        drawingService.clearCanvas();
    };

    const onSliderChange = (value: number | number[]) => {
        drawingService.setSize(value as number);
    };

    const onKeyPressed = (event: KeyboardEvent) => {
        switch (event.code) {
            case "KeyB":
                onToolSelect(ToolEnum.BRUSH);
                break;

            case "KeyF":
                onToolSelect(ToolEnum.BUCKET);
                break;
        }
    };

    return (
        <div className={"flex items-center gap-4 p-1"}>
            <Button
                disableRipple
                color={"warning"}
                className={"data-[hover=true]:opacity-100"}
                onPress={() => onToolSelect(ToolEnum.BRUSH)}
            >
                Brush
            </Button>
            <Button
                disableRipple
                color={"secondary"}
                className={"data-[hover=true]:opacity-100"}
                onPress={() => onToolSelect(ToolEnum.BUCKET)}
            >
                Bucket
            </Button>
            <Button
                disableRipple
                color={"danger"}
                className={"data-[hover=true]:opacity-100"}
                onPress={onClear}
            >
                Clear
            </Button>
            <Slider
                aria-label={"Brush Size"}
                value={drawingService.drawingStore.brushSize}
                minValue={DrawingService.MIN_BRUSH_SIZE}
                maxValue={DrawingService.MAX_BRUSH_SIZE}
                step={DrawingService.BRUSH_STEP_SIZE}
                hideValue
                showSteps
                radius={"lg"}
                color={"primary"}
                classNames={{
                    base: "w-1/3 ml-auto",
                    track: "bg-default-300",
                    step: "bg-primary"
                }}
                onChange={onSliderChange}
            />
        </div>
    );
});

export default ToolBar;