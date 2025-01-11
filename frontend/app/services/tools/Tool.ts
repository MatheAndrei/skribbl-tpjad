import type Vec2 from "~/domain/Vec2";

abstract class Tool {
    protected canvas: HTMLCanvasElement;
    protected ctx: CanvasRenderingContext2D;
    protected readonly imageData: ImageData;

    protected constructor(canvas: HTMLCanvasElement, ctx: CanvasRenderingContext2D) {
        this.canvas = canvas;
        this.ctx = ctx;
        this.imageData = ctx.getImageData(0, 0, canvas.width, canvas.height);
    }

    protected hexToRGBA(hex: string): number {
        let color = 0x000000FF;

        // map hex characters to numeric values
        const hexToNum: {[char: string]: number} = {
            "0": 0, "1": 1, "2": 2, "3": 3, "4": 4, "5": 5, "6": 6, "7": 7,
            "8": 8, "9": 9, "a": 10, "b": 11, "c": 12, "d": 13, "e": 14, "f": 15
        };

        // process each byte in reverse order to form the little-endian number
        for (let i = hex.length - 2; i >= 1; i -= 2) {
            let byte = (hexToNum[hex[i]] << 4) + hexToNum[hex[i + 1]];
            color = (color << 8) | byte;
        }

        return color >>> 0;
    }

    protected outOfBounds(pos: Vec2): boolean {
        return pos.x < 0 || pos.y < 0 || pos.x >= this.canvas.width || pos.y >= this.canvas.height;
    }

    protected getPixel(data: Uint32Array<ArrayBufferLike>, pos: Vec2): number {
        if (this.outOfBounds(pos)) return -1;

        return data[pos.y * this.canvas.width + pos.x];
    }

    protected setPixel(data: Uint32Array<ArrayBufferLike>, pos: Vec2, color: number) {
        if (this.outOfBounds(pos)) return;

        data[pos.y * this.canvas.width + pos.x] = color;
    }

    abstract onMouseDown(pos: Vec2): void;
    abstract onMouseUp(pos: Vec2): void;
    abstract onMouseMove(pos: Vec2): void;

}

export default Tool;