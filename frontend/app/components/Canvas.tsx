import {useEffect, useRef} from "react";
import type {MouseEvent} from "react";
import {drawingService} from "~/services/DrawingService";

interface CanvasProps {
    width: number;
    height: number;
    canvasColor?: string;
}

function Canvas({width, height, canvasColor}: CanvasProps) {
    const canvasRef = useRef<HTMLCanvasElement | null>(null);

    useEffect(() => {
        if (!canvasRef.current) return;

        drawingService.initCanvas(canvasRef.current, width, height, canvasColor);
    }, []);

    const onMouseDown = (event: MouseEvent<HTMLCanvasElement>) => {
        drawingService.onMouseDown(event);
    };

    const onMouseUp = (event: MouseEvent<HTMLCanvasElement>) => {
        drawingService.onMouseUp(event);
    };

    const onMouseMove = (event: MouseEvent<HTMLCanvasElement>) => {
        drawingService.onMouseMove(event);
    };

    return (
        <canvas
            ref={canvasRef}
            onMouseDown={onMouseDown}
            onMouseUp={onMouseUp}
            onMouseMove={onMouseMove}
            style={{
                width: width,
                height: height,
                // cursor: "url('data:image/svg+xml,<svg height=\"" + strokeWidth + "\" width=\"" + strokeWidth + "\" xmlns=\"http://www.w3.org/2000/svg\"><circle r=\"" + (strokeWidth / 2) + "\" cx=\"" + (strokeWidth / 2) + "\" cy=\"" + (strokeWidth / 2) + "\" fill=\"white\"/></svg>') " + (strokeWidth / 2) + " " + (strokeWidth / 2) + ", auto",
            }}
        ></canvas>
    );
}

export default Canvas;