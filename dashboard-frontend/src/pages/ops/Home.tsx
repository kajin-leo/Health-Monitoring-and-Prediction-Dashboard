import { Alert, Button, Input, Switch, Tab, Tabs } from "@heroui/react";
import { useState, useRef, useEffect } from "react";
import { apiClient } from "../../service/axios";
import { CircleCheck, CircleX } from "lucide-react";

const IndividualAttributesImport = () => {
    const [isLoading, setIsLoading] = useState(false);
    const [uploadedSuccessfully, setUploadedSuccessfully] = useState(false);
    const [uploadFailure, setUploadFailure] = useState(false);
    const [uploadFailureMessage, setUploadFailureMessage] = useState('');

    const upload = () => {
        const input = document.createElement('input');
        input.type = 'file';
        input.accept = '.csv';
        input.multiple = false;

        input.onchange = async (e) => {
            setUploadedSuccessfully(false);
            setUploadFailure(false);

            const file = e!.target!.files[0];
            if (!file) return;

            setIsLoading(true);
            const formData = new FormData();
            formData.append('file', file);
            try {
                const response = await apiClient.post('/ops/import/individual-attributes', formData, {
                    headers: {
                        'Content-Type' : 'multipart/form-data'
                    }
                });
                setIsLoading(false);
                setUploadedSuccessfully(true);
            } catch (error) {
                console.error(error);
                setIsLoading(false);
                setUploadFailure(true);
                setUploadFailureMessage(error.response.data || error.response.data.message);
            }
        }

        input.click();
    }

    return (
        <div className="flex items-center flex-col gap-2">
            <Button color='primary' className="m-2" isLoading={isLoading} onPress={upload}>
                {!isLoading ? 'Upload File' : 'Uploading'}
            </Button>
            <h3>Select the CSV file for Individual Attributes</h3>
            <h4>Please note that data of duplicate participants will be covered</h4>
            {uploadedSuccessfully ? <Alert color='success' title='Imported successfully!' /> : ''}
            {uploadFailure ? <Alert color='danger' title={uploadFailureMessage} /> : ''}
        </div>
    );
}

const BatchWorkoutImport = () => {
    return (
        <div className="flex items-center flex-col gap-2">
            <Button color='primary' className="m-2">
                Upload Files
            </Button>
            <h3>Select the CSV files for Workout Data</h3>
            <h4>Please note that data of duplicate participants will be covered</h4>
        </div>
    );
}

const IndividualWorkoutImport = () => {
    return (
        <div className="flex items-center flex-col gap-2 mt-2">
            <Input variant='bordered' isRequired
                label='Participant Index'
                placeholder="Enter the index of participant (Starting from 000000)"
                classNames={{
                    inputWrapper: [
                        'border-white/20 bg-black/10',
                        'hover:border-white',
                        'hover:bg-black/20',
                        'group-data-[focus=true]:border-white/30'
                    ]
                }}
            />
            <Button color='primary' className="m-2">
                Upload File
            </Button>
            <h3>Select the CSV file for Workout Data</h3>
            <h4>Please note that data of duplicate participants will be covered</h4>
        </div>
    );
}

const WorkoutImport = () => {
    const [isBatchImport, setBatchImport] = useState(true);
    return (
        <div className="flex items-center flex-col gap-2">
            <Switch isSelected={isBatchImport} onValueChange={setBatchImport}>
                Batch Import
            </Switch>
            {isBatchImport ? <BatchWorkoutImport /> : <IndividualWorkoutImport />}
            <h4>Only importing for existing participants is supported. </h4>
        </div>
    )
}

const TestConnection = () => {
    const [serviceRunning, setServiceRunning] = useState(false);
    useEffect(() => {
        const checkStatus = async () => {
            try {
                const response = await apiClient.get('/ops/import/status')
                console.log(response);
                if (response.status == 200) setServiceRunning(true);
            } catch (error) {
                console.error(error);
                setServiceRunning(false);
            }
        }
        checkStatus();
    }, []);

    if (serviceRunning) {
        return (
            <div className="bg-green-500/50 rounded-xl flex p-3 gap-2 text-sm items-center">
                <CircleCheck />
                Service Running
            </div>
        )
    } else {
        return (
            <div className="bg-red-600/50 rounded-xl flex p-3 gap-2 text-sm items-center">
                <CircleX />
                Service Down
            </div>
        )
    }
}

const Home = () => {

    return (
        <div className="bg-gradient-to-b from-gray-900/40 to-gray-950/30 backdrop-blur-lg rounded-2xl outline-2 outline-gray-300/20 items-center flex flex-col p-4 select-none transition-all">
            <h1 className="text-xl font-semibold m-2">Batch Import</h1>
            <Tabs variant='underlined'>
                <Tab key='individual-attributes' title='Individual Attributes'>
                    <IndividualAttributesImport />
                </Tab>
                <Tab key='workout' title='Workout'>
                    <WorkoutImport />
                </Tab>
            </Tabs>
            <div className="flex justify-end w-full">
                <TestConnection />
            </div>
        </div>
    )
}

export default Home;