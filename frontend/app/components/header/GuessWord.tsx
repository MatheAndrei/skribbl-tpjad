interface GuessWordProps {
    word: string;
    hide: boolean
}

const GuessWord = ({word, hide}: GuessWordProps) => {
    return (
        <div className={"flex text-xl font-bold"}>
            {/*{word.split("").map((char, idx) => (*/}
            {/*    <div key={`char-${idx}`} className={"w-[1ch] text-center"}>*/}
            {/*        {char}*/}
            {/*    </div>*/}
            {/*))}*/}
            {!hide && (
                <p className={"text-center"}>{word}</p>
            )}
            {hide && (
                <p className={"text-center"}>
                    {word.split("").map((char) => {
                        if (char !== " ") {
                            return "_";
                        } else {
                            return " ";
                        }
                    })}
                </p>
            )}
        </div>
    );
};

export default GuessWord;