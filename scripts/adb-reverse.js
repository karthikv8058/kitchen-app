const { exec } = require("child_process");

exec("adb devices", (error, stdout, stderr) => {
    if (error) {
        console.log(`error: ${error.message}`);
        return;
    }
    if (stderr) {
        console.log(`stderr: ${stderr}`);
        return;
    }
    let lines = stdout.split('\n');
    for (let line of lines) {
        if (line == null || line == "") {
            continue;
        }
        let device = line.split('\t');
        if(device.length == 2 && device[1] == "device"){
            console.log('\x1b[36m%s\x1b[0m', 'Executing :',`adb -s ${device[0]} reverse tcp:8081 tcp:8081`);  
            exec(`adb -s ${device[0]} reverse tcp:8081 tcp:8081`, (error, stdout, stderr) => {
                if(error){
                    console.error(`error: ${error.message}`);
                    return;
                }
                console.log('\x1b[36m%s\x1b[0m', `Completed for device (${device[0]})`);
            });
        }
    }
});