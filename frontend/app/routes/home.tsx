import type {Route} from "../../.react-router/types/app/routes/+types/home";
import {Form, Input, Button, Card, CardBody} from "@nextui-org/react";
import {useNavigate} from "react-router";

export function meta({}: Route.MetaArgs) {
    return [
        { title: "Home" },
    ];
}

function Home() {
    const navigate = useNavigate();

    return (
        <Card>
            <CardBody>
                <Form className={"grid grid-cols-3 auto-rows-fr items-center gap-4"}>
                    <Input
                        isRequired
                        label="Username"
                        variant={"faded"}
                        className={"col-span-3"}
                    />
                    <Input
                        isRequired
                        label="Room code"
                        variant={"faded"}
                        className={"col-span-2"}
                    />
                    <Button
                        className={"h-full"}
                        onPress={() => navigate("/game")}
                    >
                        Join
                    </Button>
                    <Button
                        className={"h-full col-span-3"}
                    >
                        Host
                    </Button>
                </Form>
            </CardBody>
        </Card>
    );
}

export default Home;