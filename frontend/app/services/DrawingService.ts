import type Vec2 from "~/domain/Vec2";
import type Tool from "~/services/tools/Tool";
import ToolEnum from "~/services/tools/ToolEnum";
import Brush from "~/services/tools/Brush";
import Bucket from "~/services/tools/Bucket";

export class DrawingService {
    private static readonly DEFAULT_CANVAS_COLOR = "rgb(255, 255, 255)";
    private static readonly DEFAULT_COLOR = "rgb(0, 0, 0)";
    static readonly MIN_SIZE = 1;
    static readonly MAX_SIZE = 120;
    static readonly STEP_SIZE = (DrawingService.MAX_SIZE - DrawingService.MIN_SIZE) / 7;
    static readonly DEFAULT_SIZE = DrawingService.MIN_SIZE + DrawingService.STEP_SIZE;

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

    private getMousePosition(event: MouseEvent): Vec2 {
        const {clientX, clientY} = event;
        const ratio = window.devicePixelRatio || 1;
        return {
            x: Math.round((clientX - this.canvas!.offsetLeft) * ratio),
            y: Math.round((clientY - this.canvas!.offsetTop) * ratio),
        };
    }

    initCanvas(canvas: HTMLCanvasElement, canvasColor?: string) {
        this.canvas = canvas;
        this.ctx = canvas.getContext("2d", { willReadFrequently: true })!;
        this.canvasColor = canvasColor ?? DrawingService.DEFAULT_CANVAS_COLOR;

        // support for high DPI screens
        this.resizeCanvas();

        // init style
        this.canvas.style.imageRendering = "pixelated";
        this.ctx.imageSmoothingEnabled = false;
        this.ctx.strokeStyle = DrawingService.DEFAULT_COLOR;
        this.ctx.fillStyle = DrawingService.DEFAULT_COLOR;
        this.ctx.lineWidth = DrawingService.DEFAULT_SIZE;

        this.clearCanvas();
        this.selectedTool = new Brush(this.canvas, this.ctx);
    }

    resizeCanvas() {
        if (!this.canvas || !this.ctx) return;


        const { width, height } = this.canvas.getBoundingClientRect();
        const ratio = window.devicePixelRatio || 1;

        const newWidth = width * ratio;
        const newHeight = height * ratio;

        if (this.canvas.width !== newWidth || this.canvas.height !== newHeight) {
            this.canvas.width = newWidth;
            this.canvas.height = newHeight;
            this.ctx.scale(ratio, ratio);
            this.canvas.style.width = width + "px";
            this.canvas.style.height = height + "px";
        }
    }

    clearCanvas() {
        if (!this.canvas || !this.ctx) return;

        const oldColor = this.ctx.fillStyle;
        this.ctx.fillStyle = this.canvasColor;
        this.ctx.fillRect(0, 0, this.canvas.width, this.canvas.height);
        this.ctx.fillStyle = oldColor;
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

    setColor(color: string) {
        if (!this.canvas || !this.ctx) return;

        this.ctx.strokeStyle = color;
        this.ctx.fillStyle = color;
    }

    setSize(size: number) {
        if (!this.canvas || !this.ctx) return;

        this.ctx.lineWidth = size;
    }

    onMouseDown(event: MouseEvent) {
        if (!this.selectedTool) return;

        if (event.button !== 0) {
            return;
        }

        const pos = this.getMousePosition(event);
        this.selectedTool.onMouseDown(pos);
    }

    onMouseUp(event: MouseEvent) {
        if (!this.selectedTool) return;

        if (event.button !== 0) {
            return;
        }

        const pos = this.getMousePosition(event);
        this.selectedTool.onMouseUp(pos);
    }

    onMouseMove(event: MouseEvent) {
        if (!this.canvas || !this.selectedTool) return;

        const pos = this.getMousePosition(event);
        this.selectedTool.onMouseMove(pos);
    }
}

export const drawingService = new DrawingService();