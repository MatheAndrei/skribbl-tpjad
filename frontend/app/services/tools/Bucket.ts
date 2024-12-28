import Tool from "~/services/tools/Tool";
import type Vec2 from "~/domain/Vec2";

class Bucket extends Tool {

    private readonly DIRECTIONS: Vec2[] = [
        { x:  1, y:  0 },
        { x: -1, y:  0 },
        { x:  0, y:  1 },
        { x:  0, y: -1 },
    ];

    private readonly imageData: ImageData;

    constructor(canvas: HTMLCanvasElement, ctx: CanvasRenderingContext2D) {
        super(canvas, ctx);

        this.imageData = ctx.getImageData(0, 0, canvas.width, canvas.height);
    }

    private hexToRGBA(hex: string): number[] {
        const bigint = parseInt(hex.replace("#", ""), 16);
        return [
            (bigint >> 16) & 255,
            (bigint >> 8) & 255,
            bigint & 255,
            255
        ];
    }

    private getPixelIndex(pos: Vec2): number {
        return 4 * (pos.y * this.canvas.width + pos.x);
    }

    private getPixelColor(data: Uint8ClampedArray<ArrayBufferLike>, index: number): number[] {
        return [data[index], data[index + 1], data[index + 2], data[index + 3]];
    }

    private setPixelColor(data: Uint8ClampedArray<ArrayBufferLike>, index: number, color: number[]) {
        [data[index], data[index + 1], data[index + 2], data[index + 3]] = color;
    }

    private colorsMatch(color1: number[], color2: number[]): boolean {
        return (
            color1[0] === color2[0] &&
            color1[1] === color2[1] &&
            color1[2] === color2[2] &&
            color1[3] === color2[3]
        );
    }

    private outOfBounds(pos: Vec2) {
        return (
            pos.x < 0 ||
            pos.y < 0 ||
            pos.x >= this.canvas.width ||
            pos.y >= this.canvas.height
        );
    }

    private floodFill(pos: Vec2, color: string) {
        const data = this.imageData.data;

        const targetColor = this.getPixelColor(data, this.getPixelIndex(pos));
        const replacementColor = this.hexToRGBA(color);

        // return if already colored
        if (this.colorsMatch(targetColor, replacementColor)) {
            return;
        }

        const stack: Vec2[] = [pos];

        while (stack.length > 0) {
            const currentPos = stack.pop()!;

            // set new color
            this.setPixelColor(data, this.getPixelIndex(currentPos), replacementColor);

            // navigate the neighbors
            for (let direction of this.DIRECTIONS) {
                const newPos = { x: currentPos.x + direction.x, y: currentPos.y + direction.y };
                const newPixelColor = this.getPixelColor(data, this.getPixelIndex(newPos));

                // check if new position is out of bounds
                // or pixel color is not part of the region
                if (!this.outOfBounds(newPos) && this.colorsMatch(newPixelColor, targetColor)) {
                    stack.push(newPos);
                }
            }
        }

        // update canvas
        this.ctx.putImageData(this.imageData, 0, 0);
    }

    onMouseDown(pos: Vec2) {
        this.floodFill(pos, "#fff000");
    }

    onMouseUp(pos: Vec2) {}

    onMouseMove(pos: Vec2) {}

}

export default Bucket;