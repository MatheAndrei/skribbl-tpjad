import ToolEnum from "~/services/tools/ToolEnum";
import {makeAutoObservable} from "mobx";

class DrawingStore {
    tool: ToolEnum
    color: string
    brushSize: number

    constructor(tool: ToolEnum, color: string, brushSize: number) {
        makeAutoObservable(this);

        this.tool = tool;
        this.color = color;
        this.brushSize = brushSize;
    }

    setTool(tool: ToolEnum) {
        this.tool = tool;
    }

    setColor(color: string) {
        this.color = color;
    }

    setBrushSize(size: number) {
        this.brushSize = size;
    }

}

export default DrawingStore;