import Canvas from "~/components/body/Canvas";
import Overlay from "~/components/body/overlay/Overlay";

const Body = () => {
    return (
        <div className={"relative rounded-large overflow-hidden"}>
            <Canvas/>
            <Overlay/>
        </div>
    );
};

export default Body;