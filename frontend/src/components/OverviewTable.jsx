//OverviewTable on Home page
//import * as React from 'react';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';

//get json from backend
function createData(
	destination,
	from,
	vaccine,
	dose,
	order_date,
	estimated_arrival_date,
	current_arrival_date,
	status,
	next_stop,
	urgency,
) {
	return {
		destination,
		from,
		vaccine,
		dose,
		order_date,
		estimated_arrival_date,
		current_arrival_date,
		status,
		next_stop,
		urgency,
	};
}

const rows = [
	createData('Frozen yoghurt', 159, 6.0, 24, 4.0, 16, 137, 17, 27, 27, 10),
	createData('Ice cream sandwich', 237, 9.0, 37, 4.3, 262, 16.0, 24, 6.0, 20),
];

function OverviewTable() {
	return (
		<TableContainer component={Paper}>
			<Table sx={{minWidth: 550}} aria-label='simple table'>
				<TableHead>
					<TableRow>
						<TableCell align='left'>Destination</TableCell>
						<TableCell align='left'>From</TableCell>
						<TableCell align='left'>Vaccine</TableCell>
						<TableCell align='left'>Dose</TableCell>
						<TableCell align='left'>Order Date</TableCell>
						<TableCell align='left'>Estimated Arrival Date</TableCell>
						<TableCell align='left'>Current Arrival Date</TableCell>
						<TableCell align='left'>Status</TableCell>
						<TableCell align='left'>Next Stop</TableCell>
						<TableCell align='left'>Urgency</TableCell>
					</TableRow>
				</TableHead>
				<TableBody>
					{rows.map((row) => (
						<TableRow key={row.name}>
							<TableCell align='center'>{row.destination}</TableCell>
							<TableCell align='center'>{row.from}</TableCell>
							<TableCell align='center'>{row.vaccine}</TableCell>
							<TableCell align='center'>{row.dose}</TableCell>
							<TableCell align='center'>{row.order_date}</TableCell>
							<TableCell align='center'>{row.estimated_arrival_date}</TableCell>
							<TableCell align='center'>{row.current_arrival_date}</TableCell>
							<TableCell align='center'>{row.status}</TableCell>
							<TableCell align='center'>{row.next_stop}</TableCell>
							<TableCell align='center'>{row.urgency}</TableCell>
						</TableRow>
					))}
				</TableBody>
			</Table>
		</TableContainer>
	);
}

export default OverviewTable;
