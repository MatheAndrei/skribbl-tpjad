import Tool from "~/services/tools/Tool";
import type Vec2 from "~/domain/Vec2";
import {gameService} from "~/services/GameService";

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

        let err = dx + dy;

        const radius = Math.round(width / 2);
        while (true) {
            this.drawCircle(data, pos0, radius, color);

            if (pos0.x === pos1.x && pos0.y === pos1.y) break;

            const e2 = 2 * err;
            if (e2 >= dy) {
                err += dy;
                pos0.x += sx;
            }
            if (e2 <= dx) {
                err += dx;
                pos0.y += sy;
            }
        }
    }

    private drawCircle(data: Uint32Array<ArrayBufferLike>, pos: Vec2, radius:number, color: number) {
        let x = radius;
        let y = 0;
        let dx = 1 - 2 * radius;
        let dy = 1;

        let err = dx + dy;

        while (x >= y) {
            for (let i = pos.x - x; i <= pos.x + x; i++) {
                this.setPixel(data, {x: i, y: pos.y + y}, color);
                this.setPixel(data, {x: i, y: pos.y - y}, color);
            }
            for (let i = pos.x - y; i <= pos.x + y; i++) {
                this.setPixel(data, {x: i, y: pos.y + x}, color);
                this.setPixel(data, {x: i, y: pos.y - x}, color);
            }

            if (err <= 0) {
                y++;
                dy += 2;
                err += dy;
            } else {
                x--;
                dx += 2;
                err += dx;
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

        // send to server
        gameService.sendImage(this.canvas.toDataURL());
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

        // send to server
        gameService.sendImage(this.canvas.toDataURL());
    }

}

export default Brush;