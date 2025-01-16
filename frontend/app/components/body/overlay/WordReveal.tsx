const WordReveal = () => {
    const word = "word 1";

    return (
        <div className={"h-full grid place-content-center"}>
            <p className={"flex flex-col gap-4 text-2xl text-center"}>
                <span>The word was</span>
                <span className={"font-bold text-primary"}>{word}</span>
            </p>
        </div>
    );
};

export default WordReveal;