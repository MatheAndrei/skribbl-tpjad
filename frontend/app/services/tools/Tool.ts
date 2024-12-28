import type Vec2 from "~/domain/Vec2";

abstract class Tool {
    protected canvas: HTMLCanvasElement;
    protected ctx: CanvasRenderingContext2D;

    protected constructor(canvas: HTMLCanvasElement, ctx: CanvasRenderingContext2D) {
        this.canvas = canvas;
        this.ctx = ctx;
    }

    abstract onMouseDown(pos: Vec2): void;
    abstract onMouseUp(pos: Vec2): void;
    abstract onMouseMove(pos: Vec2): void;

}

export default Tool;