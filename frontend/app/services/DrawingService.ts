import type Vec2 from "~/domain/Vec2";
import type Tool from "~/services/tools/Tool";
import ToolEnum from "~/services/tools/ToolEnum";
import Brush from "~/services/tools/Brush";
import Bucket from "~/services/tools/Bucket";
import DrawingStore from "~/stores/DrawingStore";
import {svgToUrl} from "~/utils/svgToUrl";

export class DrawingService {
    private static readonly CANVAS_RESOLUTION: Vec2 = {x: 800, y: 600};
    private static readonly CANVAS_BACKGROUND: string = "rgb(255, 255, 255)";

    private static readonly DEFAULT_TOOL: ToolEnum = ToolEnum.BRUSH;
    private static readonly DEFAULT_COLOR: string = "rgb(0, 0, 0)";

    static readonly MIN_BRUSH_SIZE: number = 1;
    static readonly MAX_BRUSH_SIZE: number = 41;
    static readonly BRUSH_STEP_SIZE: number = (DrawingService.MAX_BRUSH_SIZE - DrawingService.MIN_BRUSH_SIZE) / 7;
    static readonly DEFAULT_BRUSH_SIZE: number = DrawingService.MIN_BRUSH_SIZE + DrawingService.BRUSH_STEP_SIZE;

    private canvas: HTMLCanvasElement | null;
    private ctx: CanvasRenderingContext2D | null;
    private selectedTool: Tool | null;

    drawingStore: DrawingStore;

    constructor() {
        this.canvas = null;
        this.ctx = null;
        this.selectedTool = null;

        this.drawingStore = new DrawingStore(
            DrawingService.DEFAULT_TOOL,
            DrawingService.DEFAULT_COLOR,
            DrawingService.DEFAULT_BRUSH_SIZE
        );
    }

    initCanvas(canvas: HTMLCanvasElement) {
        this.canvas = canvas;
        this.ctx = canvas.getContext("2d", { willReadFrequently: true })!;

        // init canvas and context settings
        this.canvas.width = DrawingService.CANVAS_RESOLUTION.x;
        this.canvas.height = DrawingService.CANVAS_RESOLUTION.y;
        this.canvas.style.imageRendering = "pixelated";
        this.ctx.imageSmoothingEnabled = false;

        this.clearCanvas();
        this.setTool(DrawingService.DEFAULT_TOOL);
        this.setColor(DrawingService.DEFAULT_COLOR);
        this.setSize(DrawingService.DEFAULT_BRUSH_SIZE)
    }

    clearCanvas() {
        if (!this.canvas || !this.ctx) return;

        // clear canvas & restore previous fill color
        const oldColor = this.ctx.fillStyle;
        this.ctx.fillStyle = DrawingService.CANVAS_BACKGROUND;
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

        // update store
        this.drawingStore.setTool(tool);

        // update cursor
        this.canvas.style.cursor = this.getCursorAsUrl();
    }

    setColor(color: string) {
        if (!this.canvas || !this.ctx) return;

        this.ctx.strokeStyle = color;
        this.ctx.fillStyle = color;

        // update store
        this.drawingStore.setColor(color);

        // update cursor
        this.canvas.style.cursor = this.getCursorAsUrl();
    }

    setSize(size: number) {
        if (!this.canvas || !this.ctx) return;

        this.ctx.lineWidth = size;

        // update store
        this.drawingStore.setBrushSize(size);

        // update cursor
        this.canvas.style.cursor = this.getCursorAsUrl();
    }

    onMouseDown(event: MouseEvent) {
        if (!this.selectedTool) return;

        // skip if left click wasn't pressed
        if (event.button !== 0) {
            return;
        }

        const pos = this.getMousePosition(event);
        this.selectedTool.onMouseDown(pos);
    }

    onMouseUp(event: MouseEvent) {
        if (!this.selectedTool) return;

        // skip if left click wasn't released
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

    private getMousePosition(event: MouseEvent): Vec2 {
        const {pageX, pageY} = event;
        const rect = this.canvas!.getBoundingClientRect();

        const scaleX = this.canvas!.width / rect.width;
        const scaleY = this.canvas!.height / rect.height;

        return {
            x: Math.round((pageX - this.canvas!.offsetLeft) * scaleX),
            y: Math.round((pageY - this.canvas!.offsetTop) * scaleY),
        };
    }

    private getCursorAsUrl(): string {
        if (this.drawingStore.tool == ToolEnum.BUCKET) {
            return "crosshair";
        }

        const scale = this.canvas!.clientWidth / this.canvas!.width;
        const diameter = this.ctx!.lineWidth * scale + 1;
        const radius = diameter / 2;
        const color = this.ctx!.fillStyle;

        const svg =
            `<svg height="${diameter}" width="${diameter}" xmlns="http://www.w3.org/2000/svg">` +
            `  <circle r="${radius}" cx="${radius}" cy="${radius}" fill="${color}" fill-opacity="0.75" />` +
            `</svg>`;

        return `url(${svgToUrl(svg)}) ${radius + .25} ${radius}, crosshair`;
    }
}

export const drawingService = new DrawingService();