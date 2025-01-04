import Tool from "~/services/tools/Tool";
import type Vec2 from "~/domain/Vec2";

class Brush extends Tool {
    private isDrawing: boolean = false;
    private lastPos: Vec2 = {x: 0, y: 0};

    constructor(canvas: HTMLCanvasElement, ctx: CanvasRenderingContext2D) {
        super(canvas, ctx);
    }

    private drawLine(data: Uint32Array<ArrayBufferLike>, pos0: Vec2, pos1: Vec2, width: number, color: number) {
        const dx = Math.abs(pos1.x - pos0.x);
        const sx = pos0.x < pos1.x ? 1 : -1;
        const dy = -Math.abs(pos1.y - pos0.y);
        const sy = pos0.y < pos1.y ? 1 : -1;

        let error = dx + dy;

        const radius = Math.round(width / 2);
        while (true) {
            this.drawCircle(data, pos0, radius, color);

            if (pos0.x === pos1.x && pos0.y === pos1.y) break;

            const e2 = 2 * error;
            if (e2 >= dy) {
                error += dy;
                pos0.x += sx;
            }
            if (e2 <= dx) {
                error += dx;
                pos0.y += sy;
            }
        }
    }

    private drawCircle(data: Uint32Array<ArrayBufferLike>, pos: Vec2, radius:number, color: number) {
        let x = 0;
        let y = radius;
        let d = 3 - 2 * radius;

        const drawHorizontalLine = (x0: number, x1: number, y: number) => {
            for (let x = x0; x <= x1; x++) {
                this.setPixel(data, {x, y}, color);
            }
        }

        const fillCirclePoints = (cx: number, cy: number, x: number, y: number) => {
            drawHorizontalLine(cx - x, cx + x, cy + y);
            drawHorizontalLine(cx - x, cx + x, cy - y);
            drawHorizontalLine(cx - y, cx + y, cy + x);
            drawHorizontalLine(cx - y, cx + y, cy - x);
        };

        while (y >= x) {
            fillCirclePoints(pos.x, pos.y, x, y);
            x++;
            if (d > 0) {
                y--;
                d = d + 4 * (x - y) + 10;
            } else {
                d = d + 4 * x + 6;
            }
        }
    }

    onMouseDown(pos: Vec2) {
        const data = new Uint32Array(this.imageData.data.buffer);
        const color = this.hexToRGBA(this.ctx.strokeStyle as string);

        this.drawCircle(data, pos, Math.round(this.ctx.lineWidth / 2), color);
        this.ctx.putImageData(this.imageData, 0, 0);

        this.isDrawing = true;
        this.lastPos = pos;

    }

    onMouseUp(pos: Vec2) {
        this.isDrawing = false;
    }

    onMouseMove(pos: Vec2) {
        if (!this.isDrawing) return;

        const data = new Uint32Array(this.imageData.data.buffer);
        const color = this.hexToRGBA(this.ctx.strokeStyle as string);

        this.drawLine(data, this.lastPos, pos, this.ctx.lineWidth, color);
        this.ctx.putImageData(this.imageData, 0, 0);

        this.lastPos = pos;
    }

}

export default Brush;