import Tool from "~/services/tools/Tool";
import type Vec2 from "~/domain/Vec2";
import {gameService} from "~/services/GameService";

class Bucket extends Tool {
    constructor(canvas: HTMLCanvasElement, ctx: CanvasRenderingContext2D) {
        super(canvas, ctx);
    }

    private colorsMatch(color1: number, color2: number): boolean {
        return color1 === color2;
    }

    private floodFill(pos: Vec2, color: string) {
        const data = new Uint32Array(this.imageData.data.buffer);

        const targetColor = this.getPixel(data, pos);
        const replacementColor = this.hexToRGBA(color);

        // return if already colored
        if (this.colorsMatch(targetColor, replacementColor)) {
            return;
        }

        const stack: Vec2[] = [pos];

        while (stack.length > 0) {
            const currentPos = stack.pop()!;

            while (currentPos.y-- >= 0 && this.colorsMatch(this.getPixel(data, currentPos), targetColor)) {}

            let reachLeft = false;
            let reachRight = false;

            while (currentPos.y++ < this.canvas.height && this.colorsMatch(this.getPixel(data, currentPos), targetColor)) {
                this.setPixel(data, currentPos, replacementColor);

                if (currentPos.x > 0) {
                    if (this.colorsMatch(this.getPixel(data, {x: currentPos.x - 1, y: currentPos.y}), targetColor)) {
                        if (!reachLeft) {
                            stack.push({x: currentPos.x - 1, y: currentPos.y});
                            reachLeft = true;
                        }
                    } else {
                        reachLeft = false;
                    }
                }

                if (currentPos.x < this.canvas.width) {
                    if (this.colorsMatch(this.getPixel(data, {x: currentPos.x + 1, y: currentPos.y}), targetColor)) {
                        if (!reachRight) {
                            stack.push({x: currentPos.x + 1, y: currentPos.y});
                            reachRight = true;
                        }
                    } else {
                        reachRight = false;
                    }
                }
            }
        }

        // update canvas
        this.ctx.putImageData(this.imageData, 0, 0);
    }

    onMouseDown(pos: Vec2) {
        this.floodFill(pos, this.ctx.fillStyle as string);

        // send to server
        gameService.sendImage(this.canvas.toDataURL());
    }

    onMouseUp(pos: Vec2) {}

    onMouseMove(pos: Vec2) {}

}

export default Bucket;