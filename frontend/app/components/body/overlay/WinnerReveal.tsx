const WinnerReveal = () => {
    const winner = "player 1";

    return (
        <div className={"h-full grid place-content-center"}>
            <p className={"flex flex-col gap-4 text-2xl text-center"}>
                <span>The winner is</span>
                <span className={"font-bold text-yellow-400"}>{winner}</span>
            </p>
        </div>
    );
};

export default WinnerReveal;