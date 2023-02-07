import {createGlobalState} from 'react-hooks-global-state';

const {setGlobalState, useGlobalState} = createGlobalState({country: '', flag: '', id: 0});

export {setGlobalState, useGlobalState};
