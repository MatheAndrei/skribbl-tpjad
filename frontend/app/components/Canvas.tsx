import {useEffect, useRef} from "react";
import {drawingService} from "~/services/DrawingService";

function Canvas() {
    const canvasRef = useRef<HTMLCanvasElement | null>(null);

    useEffect(() => {
        if (!canvasRef.current) return;

        // init canvas
        const canvas = canvasRef.current;
        drawingService.initCanvas(canvasRef.current);

        // add listeners
        canvas.addEventListener("mousedown", onMouseDown);
        window.addEventListener("mouseup", onMouseUp);
        window.addEventListener("mousemove", onMouseMove);

        // free listeners
        return () => {
            canvas.removeEventListener("mousedown", onMouseDown);
            window.removeEventListener("mouseup", onMouseUp);
            window.removeEventListener("mousemove", onMouseMove);
        }
    }, []);

    const onMouseDown = (event: MouseEvent) => {
        drawingService.onMouseDown(event);
    };

    const onMouseUp = (event: MouseEvent) => {
        drawingService.onMouseUp(event);
    };

    const onMouseMove = (event: MouseEvent) => {
        drawingService.onMouseMove(event);
    };

    return (
        <canvas
            ref={canvasRef}
            className={"w-full h-auto rounded-large select-none"}
        ></canvas>
    );
}

export default Canvas;