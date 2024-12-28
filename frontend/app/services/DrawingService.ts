import type {MouseEvent} from "react";
import type Tool from "~/services/tools/Tool";
import ToolEnum from "~/services/tools/ToolEnum";
import Brush from "~/services/tools/Brush";
import Bucket from "~/services/tools/Bucket";
import type Vec2 from "~/domain/Vec2";

export class DrawingService {
    private static readonly DEFAULT_CANVAS_COLOR = "#FFFFFF";

    private canvas: HTMLCanvasElement | null;
    private ctx: CanvasRenderingContext2D | null;
    private selectedTool: Tool | null;

    private canvasColor: string;
    
    constructor() {
        this.canvas = null;
        this.ctx = null;
        this.selectedTool = null;
        this.canvasColor = DrawingService.DEFAULT_CANVAS_COLOR;
    }

    private getMousePosition(event: MouseEvent<HTMLCanvasElement>): Vec2 {
        const {offsetX, offsetY} = event.nativeEvent;
        return {x: offsetX, y: offsetY};
    }
    
    initCanvas(canvas: HTMLCanvasElement, width: number, height: number, canvasColor?: string) {
        this.canvas = canvas;
        this.ctx = canvas.getContext("2d")!;
        this.canvasColor = canvasColor ?? DrawingService.DEFAULT_CANVAS_COLOR;

        // support for high DPI screens
        const { devicePixelRatio:ratio=1 } = window;
        this.canvas.width = width * ratio;
        this.canvas.height = height * ratio;
        this.ctx.scale(ratio, ratio);

        this.clearCanvas();
        this.selectedTool = new Bucket(this.canvas, this.ctx);
    }

    clearCanvas() {
        if (!this.canvas || !this.ctx) return;

        this.ctx.fillStyle = this.canvasColor;
        this.ctx.fillRect(0, 0, this.canvas.width, this.canvas.height);
    }

    setTool(tool: ToolEnum) {
        if (!this.canvas || !this.ctx) return;

        switch (tool) {
            case ToolEnum.BRUSH:
                this.selectedTool = new Brush(this.canvas, this.ctx);
                break;
            case ToolEnum.BUCKET:
                this.selectedTool = new Bucket(this.canvas, this.ctx);
                break;
        }
    }

    onMouseDown(event: MouseEvent<HTMLCanvasElement>) {
        if (!this.selectedTool) return;

        if (event.button !== 0) {
            return;
        }

        const pos = this.getMousePosition(event);
        this.selectedTool.onMouseDown(pos);
    }

    onMouseUp(event: MouseEvent<HTMLCanvasElement>) {
        if (!this.selectedTool) return;

        if (event.button !== 0) {
            return;
        }

        const pos = this.getMousePosition(event);
        this.selectedTool.onMouseUp(pos);
    }

    onMouseMove(event: MouseEvent<HTMLCanvasElement>) {
        if (!this.selectedTool) return;

        const pos = this.getMousePosition(event);
        this.selectedTool.onMouseMove(pos);
    }
}

export const drawingService = new DrawingService();