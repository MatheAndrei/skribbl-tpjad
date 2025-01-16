import {useEffect, useRef} from "react";
import {drawingService} from "~/services/DrawingService";
import {gameService} from "~/services/GameService";
import {observer} from "mobx-react-lite";

const Canvas = observer(() => {
    const canvasRef = useRef<HTMLCanvasElement | null>(null);

    const gameStore = gameService.gameStore;

    let wheelEventCount = 0;

    useEffect(() => {
        if (!canvasRef.current) return;

        // init canvas
        const canvas = canvasRef.current;
        drawingService.initCanvas(canvasRef.current);

        // skip if not drawer
        if (!gameStore.drawing) {
            canvas.style.cursor = "initial";
            return;
        }

        // add listeners
        canvas.addEventListener("mousedown", onMouseDown);
        window.addEventListener("mouseup", onMouseUp);
        window.addEventListener("mousemove", onMouseMove);
        canvas.addEventListener("wheel", onScroll, { passive: false });

        // free listeners
        return () => {
            canvas.removeEventListener("mousedown", onMouseDown);
            window.removeEventListener("mouseup", onMouseUp);
            window.removeEventListener("mousemove", onMouseMove);
            canvas.removeEventListener("wheel", onScroll);
        }
    }, [gameStore.drawing]);

    const onMouseDown = (event: MouseEvent) => {
        drawingService.onMouseDown(event);
    };

    const onMouseUp = (event: MouseEvent) => {
        drawingService.onMouseUp(event);
    };

    const onMouseMove = (event: MouseEvent) => {
        drawingService.onMouseMove(event);
    };

    const onScroll = (event: WheelEvent) => {
        event.preventDefault();

        // check if CTRL is pressed
        if (!event.ctrlKey) return;

        // throttle
        if (wheelEventCount === 0) {
            if (event.deltaY < 0) {
                drawingService.increaseSize();
            } else {
                drawingService.decreaseSize();
            }
        }

        wheelEventCount++;
        if (wheelEventCount === 4) {
            wheelEventCount = 0;
        }
    }

    return (
        <canvas
            ref={canvasRef}
            className={"w-full h-auto rounded-large select-none"}
        ></canvas>
    );
});

export default Canvas;