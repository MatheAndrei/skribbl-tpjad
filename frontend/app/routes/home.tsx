import {type FormEvent, useState} from "react";
import type {Route} from "../../.react-router/types/app/routes/+types/home";
import {useNavigate} from "react-router";
import {Form, Input, Button, Card, CardBody} from "@nextui-org/react";
import {gameService} from "~/services/GameService";

export function meta({}: Route.MetaArgs) {
    return [
        { title: "Home" },
    ];
}

function Home() {
    const navigate = useNavigate();

    const [errors, setErrors] = useState({username: "", room: ""});


    const onSubmit = (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault();

        const submitterElem = (event.nativeEvent as SubmitEvent).submitter;
        const submitterName = (submitterElem as HTMLButtonElement).name;
        const data = Object.fromEntries(new FormData(event.currentTarget));

        switch (submitterName) {
            case "host-button":
                onHost(data as {username: string, room: string});
                break;

            case "join-button":
                onJoin(data as {username: string, room: string});
                break;
        }
    };

    const onHost = (data: {username: string, room: string}) => {
        // validate data
        if (!data.username.trim()) {
            setErrors({
                ...errors,
                username: "Please enter a username"
            });
            return;
        }

        // clear errors
        setErrors({...errors, username: ""});

        // host room
        gameService
            .host(data.username)
            .then(() => navigate("/game"))
            .catch((error) => console.log(error));
    };

    const onJoin = (data: {username: string, room: string}) => {
        // validate data
        const newErrors = {username: "", room: ""};

        if (!data.username.trim()) newErrors.username = "Please enter a username";
        if (!data.room.trim()) newErrors.room = "Please enter a room code";

        setErrors(newErrors);
        if (newErrors.username !== "" || newErrors.room !== "") {
            return;
        }

        // join room
        gameService
            .join(data.username, data.room)
            .then(() => navigate("/game"))
            .catch((error) => console.log(error));
    };

    return (
        <Card>
            <CardBody>
                <Form
                    validationBehavior={"aria"}
                    className={"grid grid-cols-3 gap-4"}
                    onSubmit={onSubmit}
                >
                    <Input
                        isInvalid={errors.username !== ""}
                        name={"username"}
                        label={"Username"}
                        errorMessage={errors.username}
                        variant={"faded"}
                        size={"sm"}
                        className={"col-span-3"}
                    />
                    <Input
                        isInvalid={errors.room !== ""}
                        name={"room"}
                        label={"Room code"}
                        errorMessage={errors.room}
                        variant={"faded"}
                        size={"sm"}
                        className={"col-span-2"}
                    />
                    <Button
                        name={"join-button"}
                        type={"submit"}
                        size={"lg"}
                    >
                        Join
                    </Button>
                    <Button
                        name={"host-button"}
                        type={"submit"}
                        size={"lg"}
                        className={"col-span-3"}
                    >
                        Host
                    </Button>
                </Form>
            </CardBody>
        </Card>
    );
}

export default Home;