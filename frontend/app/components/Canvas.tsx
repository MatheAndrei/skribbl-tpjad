import {useEffect, useRef} from "react";
import {drawingService} from "~/services/DrawingService";

interface CanvasProps {
    width: number | string;
    height: number | string;
    canvasColor?: string;
}

function Canvas({width, height, canvasColor}: CanvasProps) {
    const canvasRef = useRef<HTMLCanvasElement | null>(null);

    useEffect(() => {
        if (!canvasRef.current) return;

        // init canvas
        const canvas = canvasRef.current;
        drawingService.initCanvas(canvasRef.current, canvasColor);

        // add listeners
        canvas.addEventListener("mousedown", onMouseDown);
        window.addEventListener("mouseup", onMouseUp);
        window.addEventListener("mousemove", onMouseMove);
        window.addEventListener("resize", onWindowResize);

        return () => {
            canvas.removeEventListener("mousedown", onMouseDown);
            window.removeEventListener("mouseup", onMouseUp);
            window.removeEventListener("mousemove", onMouseMove);
            window.removeEventListener("resize", onWindowResize);
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

    const onWindowResize = () => {
        drawingService.resizeCanvas();
    }

    return (
        <canvas
            ref={canvasRef}
            className="border-3 border-gray-500 cursor-crosshair select-none"
            style={{
                width: width,
                height: height,
                // cursor: "url('data:image/svg+xml,<svg height=\"" + strokeWidth + "\" width=\"" + strokeWidth + "\" xmlns=\"http://www.w3.org/2000/svg\"><circle r=\"" + (strokeWidth / 2) + "\" cx=\"" + (strokeWidth / 2) + "\" cy=\"" + (strokeWidth / 2) + "\" fill=\"white\"/></svg>') " + (strokeWidth / 2) + " " + (strokeWidth / 2) + ", auto",
            }}
        ></canvas>
    );
}

export default Canvas;