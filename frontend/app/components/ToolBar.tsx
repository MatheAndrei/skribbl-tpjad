import {Slider} from "@nextui-org/slider";
import {DrawingService, drawingService} from "~/services/DrawingService";
import ToolEnum from "~/services/tools/ToolEnum";

function ToolBar() {
    const onSliderChange = (value: number | number[]) => {
        drawingService.setSize(value as number);
    }

    return (
        <div className={"w-1/2 flex align-top gap-4"}>
            <div style={{
                width: "100%",
                display: "flex",
                flexDirection: "column",
                gap: ".5rem",
            }}>
                <button
                    onClick={() => drawingService.setTool(ToolEnum.BRUSH)}
                    style={{
                        backgroundColor: "olive",
                    }}
                >
                    Brush
                </button>
                <Slider
                    label={"Size"}
                    defaultValue={DrawingService.DEFAULT_SIZE}
                    minValue={DrawingService.MIN_SIZE}
                    maxValue={DrawingService.MAX_SIZE}
                    step={DrawingService.STEP_SIZE}
                    hideValue={true}
                    color={"foreground"}
                    radius={"md"}
                    onChange={onSliderChange}
                />
            </div>
            <button
                onClick={() => drawingService.setTool(ToolEnum.BUCKET)}
                style={{
                    width: "100%",
                    backgroundColor: "orange",
                }}
            >
                Bucket
            </button>
        </div>
    );
}

export default ToolBar;