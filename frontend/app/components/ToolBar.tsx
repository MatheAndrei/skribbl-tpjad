import {observer} from "mobx-react-lite";
import {Button, Slider} from "@nextui-org/react";
import {DrawingService, drawingService} from "~/services/DrawingService";
import ToolEnum from "~/services/tools/ToolEnum";

const ToolBar = observer(() => {

    const onSliderChange = (value: number | number[]) => {
        drawingService.setSize(value as number);
    }

    return (
        <div className={"flex items-center gap-4"}>
            <Button
                color={"warning"}
                onPress={() => drawingService.setTool(ToolEnum.BRUSH)}
            >
                Brush
            </Button>
            <Button
                color={"secondary"}
                onPress={() => drawingService.setTool(ToolEnum.BUCKET)}
            >
                Bucket
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