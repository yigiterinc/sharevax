import {useEffect, useState} from 'react';
import Paper from '@mui/material/Paper';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TablePagination from '@mui/material/TablePagination';
import TableRow from '@mui/material/TableRow';
import {fetchActiveDeliveries} from '../services/services';
const columns = [
	{id: 'destinationHarbor', label: 'Destination', minWidth: 10},
	{id: 'startHarbor', label: 'From', minWidth: 21},
	{id: 'vaccineType', label: 'Vaccine', minWidth: 10},
	{id: 'quantity', label: 'Dose', minWidth: 20},
	{id: 'createdAt', label: 'Order Date', minWidth: 21},
	{id: 'estimatedArrivalDate', label: 'Estimated Arrival Date', minWidth: 21},
	{id: 'remainingDaysToNextHarbor', label: 'Current Arrival Date', minWidth: 21},
	{id: 'deliveryStatus', label: 'Status', minWidth: 21},
	{id: 'urgency', label: 'Urgency', minWidth: 21},
	{id: 'updatedAt', label: 'Updated At', minWidth: 21},
];

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
	createData(
		'Germany',
		'Thailand',
		'A',
		2000,
		'2022.4.0',
		'2022.4.1',
		'2022.5.1',
		'2022.5.1',
		'Shipping',
		'France',
		'red',
	),
	createData(
		'China',
		'USA',
		'C',
		5000,
		'2022.2.1',
		'2022.2.2',
		'2022.3.1',
		'2022.3.1',
		'Shipping',
		'Hong Kong',
		'green',
	),
	createData(
		'Ukase',
		'Germany',
		'B',
		8000,
		'2022.6.1',
		'2022.6.2',
		'2022.6.1',
		'2022.3.1',
		'Shipping',
		'India',
		'grey',
	),
	createData(
		'Germany',
		'Thailand',
		'A',
		2000,
		'2022.4.0',
		'2022.4.1',
		'2022.5.1',
		'2022.5.1',
		'Shipping',
		'France',
		'red',
	),
	createData(
		'China',
		'USA',
		'C',
		5000,
		'2022.2.1',
		'2022.2.2',
		'2022.3.1',
		'2022.3.1',
		'Shipping',
		'Hong Kong',
		'green',
	),
	createData(
		'Ukase',
		'Germany',
		'B',
		8000,
		'2022.6.1',
		'2022.6.2',
		'2022.6.1',
		'2022.3.1',
		'Shipping',
		'India',
		'grey',
	),
	createData(
		'Germany',
		'Thailand',
		'A',
		2000,
		'2022.4.0',
		'2022.4.1',
		'2022.5.1',
		'2022.5.1',
		'Shipping',
		'France',
		'red',
	),
];

export default function OverviewTable() {
	const [page, setPage] = useState(0);
	const [rowsPerPage, setRowsPerPage] = useState(10);
	const [activeDeliveriesData, setActiveDeliveriesData] = useState([]);

	useEffect(() => {
		fetchActiveDeliveriesData();
	}, []);

	const fetchActiveDeliveriesData = async () => {
		const result = await fetchActiveDeliveries();
		setActiveDeliveriesData(result.data);
	};

	const rowData = [
		{
			destinationHarbor: activeDeliveriesData.destinationHarbor.countryName,
			startHarbor: activeDeliveriesData.startHarbor.countryName,
			vaccineType: activeDeliveriesData.vaccineType,
			quantity: activeDeliveriesData.quantity,
			createdAt: activeDeliveriesData.createdAt,
			estimatedArrivalDate: activeDeliveriesData.estimatedArrivalDate,
			remainingDaysToNextHarbor: activeDeliveriesData.remainingDaysToNextHarbor,
			deliveryStatus: activeDeliveriesData.deliveryStatus,
			urgency: activeDeliveriesData.urgency,
			updatedAt: activeDeliveriesData.updatedAt,
		},
	];
	const handleChangePage = (event, newPage) => {
		setPage(newPage);
	};

	const handleChangeRowsPerPage = (event) => {
		setRowsPerPage(+event.target.value);
		setPage(0);
	};

	return (
		<Paper sx={{width: '100%', overflow: 'hidden'}}>
			<div className='text-bg text-white p-2 bg-black font-mono font-bold'>Vaccines Currently Shipping</div>
			<TableContainer sx={{maxHeight: 260}}>
				<Table stickyHeader aria-label='sticky table'>
					<TableHead>
						<TableRow>
							{columns.map((column) => (
								<TableCell
									key={column.id}
									align={column.align}
									style={{minWidth: column.minWidth}}
									className='font-bold text-white bg-black border-t border-gray-500'
								>
									{column.label}
								</TableCell>
							))}
						</TableRow>
					</TableHead>
					<TableBody>
						{rowData.slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage).map((row) => {
							return (
								<TableRow hover role='checkbox' tabIndex={-1} key={row.code}>
									{columns.map((column) => {
										const value = row[column.id];
										return (
											<TableCell key={column.id} align={column.align} className='font-mono'>
												{column.format && typeof value === 'number' ? column.format(value) : value}
											</TableCell>
										);
									})}
								</TableRow>
							);
						})}
					</TableBody>
				</Table>
			</TableContainer>
			<TablePagination
				rowsPerPageOptions={[5, 10, 20]}
				component='div'
				count={rows.length}
				rowsPerPage={rowsPerPage}
				page={page}
				onPageChange={handleChangePage}
				onRowsPerPageChange={handleChangeRowsPerPage}
			/>
		</Paper>
	);
}
