import Tool from "~/services/tools/Tool";
import type Vec2 from "~/domain/Vec2";

class Brush extends Tool {
    private isDrawing: boolean = false;
    private lastPos: Vec2 = {x: 0, y: 0};

    constructor(canvas: HTMLCanvasElement, ctx: CanvasRenderingContext2D) {
        super(canvas, ctx);

        // set style
        ctx.strokeStyle = "#ffffff";
        ctx.fillStyle = "#ffffff";
        ctx.lineWidth = 10;
        ctx.lineCap = "round";
        ctx.lineJoin = "round";
    }

    onMouseDown(pos: Vec2) {
        this.ctx.beginPath();
        this.ctx.arc(pos.x, pos.y, 10 / 2, 0, 2 * Math.PI);
        this.ctx.fill();

        this.isDrawing = true;
        this.lastPos = pos;
    }

    onMouseUp(pos: Vec2) {
        this.isDrawing = false;
    }

    onMouseMove(pos: Vec2) {
        if (!this.isDrawing) return;

        this.ctx.beginPath();
        this.ctx.moveTo(this.lastPos.x, this.lastPos.y);
        this.ctx.lineTo(pos.x, pos.y);
        this.ctx.stroke();

        this.lastPos = pos;
    }

}

export default Brush;